package dev.kord.build.cache

import dev.kord.build.createProject
import jetbrains.buildServer.configs.kotlin.Project
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.VcsRoot
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

val Cache = createProject("cache", "feature/native") {
    project.id = RelativeId("cache")
    addBuildType {
        name = "Build & Push"
        id = RelativeId("build")
        steps {
            gradle {
                id = "checks"
                tasks = "check"
                jdkHome = "%env.JDK_11_0%"
            }
        }
    }
}
