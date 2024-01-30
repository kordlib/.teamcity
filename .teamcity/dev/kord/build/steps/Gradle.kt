package dev.kord.build.steps

import dev.kord.build.ProjectContext
import dev.kord.build.applyMultiplatformMatrix
import jetbrains.buildServer.configs.kotlin.BuildFeatures
import jetbrains.buildServer.configs.kotlin.BuildSteps
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle

fun ProjectContext.runTestsAndPublishWithGradle(
    features: BuildFeatures.() -> Unit = {},
    beforeSteps: BuildSteps.() -> Unit = {}
) = addBuildType {
    applyMultiplatformMatrix()

    name = "Build & Push"
    id = childId("build")

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
