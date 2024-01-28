package dev.kord.build.cache

import dev.kord.build.applyMultiplatformMatrix
import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.qodana

val Cache = createProject("cache", "feature/native") {
    project.params.applyPublishingParameters()

    project.id = RelativeId("cache")
    addBuildType {
        name = "Code Quality"
        id = RelativeId("qodana")

        requirements {
            matches("teamcity.agent.os.family", "Linux")
        }

        steps {
            qodana {
                linter = jvm()
                cloudToken = "credentialsJSON:3a7564f6-e577-4553-a387-80c2686bde05"
                additionalQodanaArguments = "--baseline .ci/qodana.sarif.json"
            }
        }
    }
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
            gradle {
                id = "publish"
                tasks = "publishForCurrentOs"
                jdkHome = "%env.JDK_11_0%"
            }
        }
    }
}
