package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a buildType with id = 'Lavalink_Build'
in the project with id = 'Lavalink', and delete the patch script.
*/
create(RelativeId("Lavalink"), BuildType({
    id("Lavalink_Build")
    name = "Build"

    vcs {
        root(RelativeId("Lavalink_HttpsGithubComKordlibLavalinkKtRefsHeadsMain"))
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
}))

