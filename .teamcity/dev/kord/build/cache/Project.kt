package dev.kord.build.cache

import dev.kord.build.applyMultiplatformMatrix
import dev.kord.build.createProject
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle

val Cache = createProject("cache", "feature/native") {
    project.id = RelativeId("cache")
    addBuildType {
        applyMultiplatformMatrix()

        name = "Build & Push"
        id = RelativeId("build")
        steps {
            gradle {
                id = "checks"
                tasks = "testOnCurrentOS"
                jdkHome = "%env.JDK_11_0%"
            }
        }
    }
}
