package dev.kord.build

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.Project
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.failureConditions.BuildFailureOnMetric
import jetbrains.buildServer.configs.kotlin.failureConditions.failOnMetricChange
import jetbrains.buildServer.configs.kotlin.matrix
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

data class ProjectContext(val vcsRoot: GitVcsRoot, val project: Project) {
    fun addBuildType(additionalConfig: BuildType.() -> Unit) = with(project) {
        buildType {
            triggers {
                vcs { }
            }
            vcs {
                root(vcsRoot)
            }

            failureConditions {
                failOnMetricChange {
                    metric = BuildFailureOnMetric.MetricType.TEST_FAILED_COUNT
                    units = BuildFailureOnMetric.MetricUnit.DEFAULT_UNIT
                    comparison = BuildFailureOnMetric.MetricComparison.MORE
                    compareTo = build {
                        buildRule = lastSuccessful()
                    }
                }
            }

            features {
                commitStatusPublisher {
                    vcsRootExtId = "${vcsRoot.id}"
                    publisher = github {
                        githubUrl = "https://api.github.com"
                        authType = storedToken {
                            tokenId =
                                "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:7ca49a71-b885-4a77-a5b8-ccb6108471d1"
                        }
                    }
                }

                pullRequests {
                    vcsRootExtId = "${vcsRoot.id}"
                    provider = github {
                        authType = storedToken {
                            tokenId =
                                "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:7ca49a71-b885-4a77-a5b8-ccb6108471d1"
                        }
                        filterAuthorRole = PullRequests.GitHubRoleFilter.EVERYBODY
                    }
                }
            }

            additionalConfig()
        }
    }
}

/**
 * Configures this build to run on `Windows`, `Mac OS` and `Linux`
 */
fun BuildType.applyMultiplatformMatrix() = features {
    matrix {
        os = listOf(
            value("Linux"),
            value("Mac OS"),
            value("Windows")
        )
    }
}

fun createProject(
    name: String,
    defaultBranch: String,
    projectConfigurator: ProjectContext.() -> Unit = {}
): Project {
    val vcsRoot = GitVcsRoot {
        id("${name}VcsRoot")
        this.name = "kordlib/$name"
        url = "https://github.com/kordlib/$name"
        branch = "refs/heads/$defaultBranch"
        branchSpec = "refs/heads/*"
        authMethod = token {
            userName = "oauth2"
            tokenId = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:57833e23-d218-4f37-ac17-55edd9393949"
        }
    }

    return Project {
        this.name = name
        parentId = DslContext.parentProjectId

        vcsRoot(vcsRoot)
        ProjectContext(vcsRoot, this).apply(projectConfigurator)
    }
}
