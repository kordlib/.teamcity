package dev.kord.build.kord

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import dev.kord.build.steps.qodana
import dev.kord.build.steps.runTestsAndPublishWithGradle
import jetbrains.buildServer.configs.kotlin.RelativeId

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:14179102-c8b1-4427-82dc-d7ab819ae5d7"

val Kord = createProject("kord", "main", token) {
    project.params.applyPublishingParameters()

    project.id = RelativeId("kord")
    qodana("credentialsJSON:bcc43ac3-b408-44d2-bb5a-361fe53b5652")
    runTestsAndPublishWithGradle()
}
