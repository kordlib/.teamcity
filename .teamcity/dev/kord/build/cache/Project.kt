package dev.kord.build.cache

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import dev.kord.build.steps.qodana
import dev.kord.build.steps.runTestsAndPublishWithGradle
import jetbrains.buildServer.configs.kotlin.RelativeId

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:7ca49a71-b885-4a77-a5b8-ccb6108471d1"

val Cache = createProject("cache", "main", token) {
    project.params.applyPublishingParameters()

    project.id = RelativeId("cache")
    qodana("credentialsJSON:3a7564f6-e577-4553-a387-80c2686bde05")
    runTestsAndPublishWithGradle()
}
