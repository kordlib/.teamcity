package dev.kord.build.lavalink

import dev.kord.build.createProject
import dev.kord.build.steps.runTestsAndPublishWithGradle
import jetbrains.buildServer.configs.kotlin.RelativeId

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:f377899c-08d9-45ed-8816-a3ef0077239e"

val Lavalink = createProject("Lavalink.kt", "main", token) {
    project.id = RelativeId("LavalinkKt")

    runTestsAndPublishWithGradle(useSeparateHosts = false)
}
