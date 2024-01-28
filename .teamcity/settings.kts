import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubAppConnection
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11"

project {
    description = "Contains all other projects"

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
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

    cleanup {
        baseRule {
            all(days = 365)
            history(days = 90)
            preventDependencyCleanup = false
        }
    }

    subProject(Cache)
}


object Cache : Project({
    name = "Cache"

    vcsRoot(Cache_HttpsGithubComKordlibCacheRefsHeadsMaster)

    buildType(Cache_Build)
})

object Cache_Build : BuildType({
    name = "Build"

    vcs {
        root(Cache_HttpsGithubComKordlibCacheRefsHeadsMaster)
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object Cache_HttpsGithubComKordlibCacheRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/kordlib/cache#refs/heads/master"
    url = "https://github.com/kordlib/cache"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "DRSchlaubi"
        password = "credentialsJSON:abb87df6-3a83-4342-b3fa-41c919da36a0"
    }
})
