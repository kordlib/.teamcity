package dev.kord.build.emoji

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import dev.kord.build.steps.runTestsAndPublishWithGradle
import jetbrains.buildServer.configs.kotlin.RelativeId

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:92aea938-40e1-421f-8926-79db8fa10b8d"

val Emoji = createProject("emoji", "main", token, gitName = "kordx.emoji") {
    project.params.applyPublishingParameters()

    project.id = RelativeId("emoji")
    runTestsAndPublishWithGradle()
}
