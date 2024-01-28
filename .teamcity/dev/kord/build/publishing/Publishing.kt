package dev.kord.build.publishing

import jetbrains.buildServer.configs.kotlin.ParameterDisplay
import jetbrains.buildServer.configs.kotlin.ParametrizedWithType

fun ParametrizedWithType.applyPublishingParameters() {
    secretEnv("ORG_GRADLE_PROJECT_mavenCentralUsername", "credentialsJSON:732f0780-6752-42bc-a222-48c171a06b19")
    secretEnv("ORG_GRADLE_PROJECT_mavenCentralPassword", "credentialsJSON:56a86492-6c2d-4ed4-8121-6fdec2cb1155")
    secretEnv("ORG_GRADLE_PROJECT_signingInMemoryKey", "credentialsJSON:ddba5b0d-4fc1-4e83-b94f-748c53b0f1e0")
    secretEnv(
        "ORG_GRADLE_PROJECT_signingInMemoryKeyPassword",
        "credentialsJSON:05652fab-9c46-41aa-9688-681a3d26f2d4"
    )
}

private fun ParametrizedWithType.secretEnv(name: String, value: String) =
    password("env.$name", value, readOnly = true, display = ParameterDisplay.HIDDEN)