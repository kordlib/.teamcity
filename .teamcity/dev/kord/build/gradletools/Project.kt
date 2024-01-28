package dev.kord.build.gradletools

import dev.kord.build.createProject
import dev.kord.build.publishing.secretEnv
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.triggers.vcs

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:f6e5b12a-e9be-4fa4-a194-76780560b03e"

val GradleTools = createProject("gradle-tools", "main", token) {
    project.params {
        secretEnv("env.GOOGLE_KEY", "credentialsJSON:4b25a972-2a52-4241-8b08-fe0752dabc67")
    }

    project.id = RelativeId("GradleTools")
    addBuildType(customTrigger = true) {
        name = "Build & Push"
        id = childId("build")

        triggers {
            vcs {
                branchFilter = "main"
            }
        }
        steps {
            gradle {
                id = "publish"
                tasks = "publish"
                jdkHome = "%env.JDK_11_0%"
            }
        }
    }
}
