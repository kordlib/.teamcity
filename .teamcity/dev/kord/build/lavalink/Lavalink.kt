package dev.kord.build.lavalink

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import dev.kord.build.steps.runTestsAndPublishWithGradle
import jetbrains.buildServer.configs.kotlin.RelativeId

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:0224f02c-a618-4782-8239-75364ab55f97"

val Lavalink = createProject("Lavalink.kt", "main", token) {
    project.params.applyPublishingParameters(usePublisherPortal = true)
    project.id = RelativeId("LavalinkKt")

    runTestsAndPublishWithGradle(useSeparateHosts = false)
}
