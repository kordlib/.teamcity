package dev.kord.build.steps

import dev.kord.build.ProjectContext
import dev.kord.build.applyMultiplatformMatrix
import jetbrains.buildServer.configs.kotlin.BuildFeatures
import jetbrains.buildServer.configs.kotlin.BuildSteps
import jetbrains.buildServer.configs.kotlin.ParameterDisplay
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.triggers.vcs

fun ProjectContext.runTestsAndPublishWithGradle(
    onlyOnBranch: String? = null,
    features: BuildFeatures.() -> Unit = {},
    beforeSteps: BuildSteps.() -> Unit = {}
) {
    // https://youtrack.jetbrains.com/issue/TW-75263/#focus=Comments-27-9187287.0-0
     // TODO: Remove once TW-86481 is fixed
    val bugfix = addBuildType(onlyOnBranch != null) {
        id = childId("bugfix")
        name = "Bugfix"
    }
    addBuildType(onlyOnBranch != null) {
        params {
            text("env.PR_BRANCH", "%dep.${bugfix.id}.teamcity.pullRequest.source.branch%",
                display = ParameterDisplay.HIDDEN,
                allowEmpty = true
            )
        }
        dependencies {
            dependency(bugfix) {
                snapshot {
                    runOnSameAgent = false
                }
            }
        }
        applyMultiplatformMatrix()

        name = "Build & Push"
        id = childId("build")

        triggers {
            vcs {
                branchFilter = "|+:$onlyOnBranch"
            }
        }

        features(features)
        steps {
            beforeSteps()
            gradle {
                id = "checks"
                tasks = "testOnCurrentOS"
                jdkHome = "%env.JDK_11_0%"
            }
            gradle {
                id = "publish"
                tasks = "publishForCurrentOs"
                jdkHome = "%env.JDK_11_0%"
            }
        }
    }
}
