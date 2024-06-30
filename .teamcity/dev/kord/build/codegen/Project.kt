package dev.kord.build.codegen

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.triggers.vcs

private val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:ba37a7ce-9c76-4146-9dc7-f6d1dd01ed6b"

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
