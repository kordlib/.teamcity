package dev.kord.build.gradletools

import dev.kord.build.createProject
import dev.kord.build.publishing.secretEnv
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.triggers.vcs

val GradleTools = createProject("gradle-tools", "main") {
    project.params {
        secretEnv("env.GOOGLE_KEY", "credentialsJSON:4b25a972-2a52-4241-8b08-fe0752dabc67")
    }
    project.id = RelativeId("GradleTools")
    addBuildType {
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
