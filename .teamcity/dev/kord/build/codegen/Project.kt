package dev.kord.build.codegen

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.triggers.vcs

private val token = ""

val CodegenKt = createProject("CodeGen.kt", "main", token, "codegen-kt") {
    project.params.applyPublishingParameters()

    project.id = RelativeId("codegen")
    addBuildType(customTrigger = true) {
        name = "Build & Push"
        id = childId("build")

        triggers {
            vcs {
                branchFilter = "+:main"
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
