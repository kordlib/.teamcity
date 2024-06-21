package dev.kord.build

import dev.kord.build.utils.toCamelCase
import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.failureConditions.BuildFailureOnMetric
import jetbrains.buildServer.configs.kotlin.failureConditions.failOnMetricChange
import jetbrains.buildServer.configs.kotlin.projectFeatures.UntrustedBuildsSettings
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubIssues
import jetbrains.buildServer.configs.kotlin.projectFeatures.untrustedBuildsSettings
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
                        +:<default>
                        +:pull/*
                        +:tags/*
                    """.trimIndent()
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
    gitName: String = name,
    projectConfigurator: ProjectContext.() -> Unit = {}
): Project {
    val vcsRoot = GitVcsRoot {
        id("${name.toCamelCase().replace("\\W".toRegex(), "")}VcsRoot")
        this.name = "kordlib/$name"
        url = "https://github.com/kordlib/$gitName"
        branch = "refs/heads/$defaultBranch"
        checkoutPolicy = GitVcsRoot.AgentCheckoutPolicy.USE_MIRRORS
        branchSpec = """
            +:refs/heads/*
            +:refs/tags/*
        """.trimIndent()
        useTagsAsBranches = true
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

            untrustedBuildsSettings {
                defaultAction = UntrustedBuildsSettings.DefaultAction.APPROVE
                enableLog = true
                timeoutMinutes = 7200 // No idea what best timeout would be, let's specify 5 days for now
                approvalRules = "group:CODE_REVIEWERS:1"
            }
        }

        vcsRoot(vcsRoot)
        ProjectContext(vcsRoot, this, projectToken).apply(projectConfigurator)
    }
}
