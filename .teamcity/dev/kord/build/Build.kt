package dev.kord.build

import dev.kord.build.utils.toCamelCase
import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.buildCache
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.failureConditions.BuildFailureOnMetric
import jetbrains.buildServer.configs.kotlin.failureConditions.failOnMetricChange
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubIssues
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

data class ProjectContext(val vcsRoot: GitVcsRoot, val project: Project, val token: String) {
    /**
     * Creates a new [RelativeId] with this project's id as its parent.
     */
    fun childId(name: String) = AbsoluteId("${project.id!!.value}_$name")
    fun addBuildType(customTrigger: Boolean = false, additionalConfig: BuildType.() -> Unit) = with(project) {
        buildType {
            if (!customTrigger) {
                triggers {
                    vcs {
                        branchFilter = """
                        |+:master
                        |+:pull/*
                        |+:tags/*
                    """.trimMargin()
                    }
                }
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
                failOnMetricChange {
                    metric = BuildFailureOnMetric.MetricType.TEST_COUNT
                    threshold = 10
                    units = BuildFailureOnMetric.MetricUnit.PERCENTS
                    comparison = BuildFailureOnMetric.MetricComparison.LESS
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
                            tokenId = token
                        }
                    }
                }

                pullRequests {
                    vcsRootExtId = "${vcsRoot.id}"
                    provider = github {
                        authType = storedToken {
                            tokenId = token
                        }
                        filterAuthorRole = PullRequests.GitHubRoleFilter.EVERYBODY
                    }
                }
                buildCache {
                    name = "GradleCache"
                    rules = """
                        gradle-home/caches
                        gradle-home/wrapper/dists
                        gradle-home/yarn
                        buildSrc/build/kotlin
                        buildSrc/build/kotlin-dsl
                    """.trimIndent()
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
    projectToken: String,
    projectConfigurator: ProjectContext.() -> Unit = {}
): Project {
    val vcsRoot = GitVcsRoot {
        id("${name.toCamelCase()}VcsRoot")
        this.name = "kordlib/$name"
        url = "https://github.com/kordlib/$name"
        branch = "refs/heads/$defaultBranch"
        branchSpec = """
            |+:refs/pull/*/head
            |+:refs/heads/*
        """.trimIndent()
        authMethod = token {
            userName = "oauth2"
            tokenId = projectToken
        }
    }

    return Project {
        this.name = name
        parentId = DslContext.parentProjectId

        features {
            githubIssues {
                displayName = "kordlib/$name"
                repositoryURL = "https://github.com/kordlib/$name"
                issuesPattern = "#(\\d+)"

                authType = storedToken {
                    tokenId = projectToken
                }
            }
        }

        vcsRoot(vcsRoot)
        ProjectContext(vcsRoot, this, projectToken).apply(projectConfigurator)
    }
}
