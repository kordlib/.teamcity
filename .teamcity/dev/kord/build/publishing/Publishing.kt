package dev.kord.build.publishing

import jetbrains.buildServer.configs.kotlin.ParameterDisplay
import jetbrains.buildServer.configs.kotlin.ParametrizedWithType

fun ParametrizedWithType.applyPublishingParameters(
    useS01Credentials: Boolean = false,
    usePublisherPortal: Boolean = false
) {
    if (usePublisherPortal) {
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralUsername", "credentialsJSON:5d1dd4b3-3455-4eb5-a415-ce8d211f4cf8")
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralPassword", "credentialsJSON:199eeb02-83c3-46ab-b29b-e459b1effe6c")
    } else if (useS01Credentials) {
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralUsername", "credentialsJSON:ea81b569-741e-409f-bed9-00fc98319845")
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralPassword", "credentialsJSON:0deeae31-c62a-446d-83d8-d978d6f69ad5")
    } else {
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralUsername", "credentialsJSON:07af3728-e7aa-43b3-9399-b63c3ce48964")
        secretEnv("ORG_GRADLE_PROJECT_mavenCentralPassword", "credentialsJSON:fe8abef7-4f73-4576-bd76-34a09346b2ff")
    }
    secretEnv("ORG_GRADLE_PROJECT_signingInMemoryKey", "credentialsJSON:ddba5b0d-4fc1-4e83-b94f-748c53b0f1e0")
    secretEnv(
        "ORG_GRADLE_PROJECT_signingInMemoryKeyPassword",
        "credentialsJSON:52929ed0-01b0-4025-85d9-5bf69c42cb79"
    )
}

fun ParametrizedWithType.secretEnv(name: String, value: String) =
    password("env.$name", value, readOnly = true, display = ParameterDisplay.HIDDEN)