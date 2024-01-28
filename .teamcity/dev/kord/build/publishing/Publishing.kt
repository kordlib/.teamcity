package dev.kord.build.publishing

import jetbrains.buildServer.configs.kotlin.Parametrized

fun Parametrized.applyPublishingParameters() {
    param("env.NEXUS_USER", "credentialsJSON:732f0780-6752-42bc-a222-48c171a06b19")
    param("env.NEXUS_PASSWORD", "credentialsJSON:56a86492-6c2d-4ed4-8121-6fdec2cb1155")
    param("env.SIGNING_KEY", "credentialsJSON:ddba5b0d-4fc1-4e83-b94f-748c53b0f1e0")
    param("env.SIGNING_PASSWORD", "credentialsJSON:52929ed0-01b0-4025-85d9-5bf69c42cb79")
}
