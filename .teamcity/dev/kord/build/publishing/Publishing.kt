package dev.kord.build.publishing

import jetbrains.buildServer.configs.kotlin.ParameterDisplay
import jetbrains.buildServer.configs.kotlin.ParametrizedWithType

fun ParametrizedWithType.applyPublishingParameters(usePublisherPortal: Boolean = false) {
    if (usePublisherPortal) {
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralUsername", "credentialsJSON:74b2fea6-b88d-4d1f-a357-b96aa4e6b3e2")
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralPassword", "credentialsJSON:773c3b77-79dd-43d5-bd82-60d09c88a6c5")
    } else {
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralUsername", "credentialsJSON:ea81b569-741e-409f-bed9-00fc98319845")
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralPassword", "credentialsJSON:0deeae31-c62a-446d-83d8-d978d6f69ad5")
    }
    secretEnv("ORG_GRADLE_PROJECT_signingInMemoryKey", "credentialsJSON:ddba5b0d-4fc1-4e83-b94f-748c53b0f1e0")
    secretEnv(
        "ORG_GRADLE_PROJECT_signingInMemoryKeyPassword",
        "credentialsJSON:52929ed0-01b0-4025-85d9-5bf69c42cb79"
    )
}

fun ParametrizedWithType.secretEnv(name: String, value: String) =
    password("env.$name", value, readOnly = true, display = ParameterDisplay.HIDDEN)