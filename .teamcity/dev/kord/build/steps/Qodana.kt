package dev.kord.build.steps

import dev.kord.build.ProjectContext
import jetbrains.buildServer.configs.kotlin.buildSteps.qodana

fun ProjectContext.qodana(token:String) = addBuildType {
    name = "Code Quality"
    id = childId("qodana")

    requirements {
        matches("teamcity.agent.os.family", "Linux")
    }

    steps {
        qodana {
            linter = jvm()
            cloudToken = token
        }
    }
}
