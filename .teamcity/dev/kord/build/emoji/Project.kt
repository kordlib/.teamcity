package dev.kord.build.emoji

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import dev.kord.build.steps.runTestsAndPublishWithGradle
import jetbrains.buildServer.configs.kotlin.RelativeId

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:363e801f-3ff6-46a2-af59-05ed7b5cbb56"

val Emoji = createProject("emoji", "main", token, gitName = "kordx.emoji") {
    project.params.applyPublishingParameters()

    project.id = RelativeId("emoji")
    runTestsAndPublishWithGradle()
}
