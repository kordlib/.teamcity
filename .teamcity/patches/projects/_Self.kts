package patches.projects

import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.Project
import jetbrains.buildServer.configs.kotlin.projectFeatures.GitHubAppConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubAppConnection
import jetbrains.buildServer.configs.kotlin.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the root project
accordingly, and delete the patch script.
*/
changeProject(DslContext.projectId) {
    features {
        val feature1 = find<GitHubAppConnection> {
            githubAppConnection {
                id = "PROJECT_EXT_4"
                displayName = "Kord CI"
                appId = "809462"
                clientId = "Iv1.b5b3e496bb419067"
                clientSecret = "credentialsJSON:ef09ef86-f726-49b1-92ba-27d76c4fb8a9"
                privateKey = "credentialsJSON:fae145c9-9350-47e5-b141-ac124ad4a7ff"
                webhookSecret = "credentialsJSON:b10aab19-a343-4410-a009-f3b19fb80c18"
                ownerUrl = "https://github.com/kordlib"
            }
        }
        feature1.apply {
        }
    }
}
