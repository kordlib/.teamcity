package dev.kord.build.steps

import dev.kord.build.ProjectContext
import dev.kord.build.applyMultiplatformMatrix
import jetbrains.buildServer.configs.kotlin.BuildFeatures
import jetbrains.buildServer.configs.kotlin.BuildSteps
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.triggers.vcs

fun ProjectContext.runTestsAndPublishWithGradle(
    onlyOnBranch: String? = null,
    useSeparateHosts: Boolean = true,
    features: BuildFeatures.() -> Unit = {},
    beforeSteps: BuildSteps.() -> Unit = {}
) {
    addBuildType(onlyOnBranch != null) {
        if (useSeparateHosts) {
            applyMultiplatformMatrix()
        }

        name = "Build & Push"
        id = childId("build")

        if (onlyOnBranch != null) {
            triggers {
                vcs {
                    branchFilter = "+:$onlyOnBranch"
                }
            }
        }

        features(features)
        steps {
            beforeSteps()
            gradle {
                id = "checks"
                tasks = "testOnCurrentOS"
                jdkHome = "%env.JDK_17_0%"
            }
            gradle {
                id = "publish"
                tasks = "publishForCurrentOs"
                jdkHome = "%env.JDK_17_0%"
            }
        }
    }
}
