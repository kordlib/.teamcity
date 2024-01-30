package dev.kord.build.kord

import dev.kord.build.createProject
import dev.kord.build.publishing.applyPublishingParameters
import dev.kord.build.steps.qodana
import dev.kord.build.steps.runTestsAndPublishWithGradle
import jetbrains.buildServer.configs.kotlin.RelativeId
import jetbrains.buildServer.configs.kotlin.buildFeatures.buildCache
import jetbrains.buildServer.configs.kotlin.buildSteps.script

private const val token = "tc_token_id:CID_ead29039499734d5f53ebb99e1e14bf5:-1:14179102-c8b1-4427-82dc-d7ab819ae5d7"

val Kord = createProject("kord", "main", token) {
    project.params.applyPublishingParameters()

    project.id = RelativeId("kord")
    qodana("credentialsJSON:bcc43ac3-b408-44d2-bb5a-361fe53b5652")
    runTestsAndPublishWithGradle({
        buildCache {
            name = "curl_build"
            rules = "curl_build"
        }
    }) {
        script {
            name = "Install curl"
            conditions {
                matches("teamcity.agent.os.family", "Linux")
            }

            workingDir = "curl_build"
            scriptContent = """
                if [ ! -e Makefile ]; then
                    curl -o curl.tgz https://curl.se/download/curl-8.5.0.tar.gz
                    tar xzvf curl.tgz --strip-components=1
                    rm curl.tgz
                    ./configure --with-openssl --enable-websockets
                fi
                if [ ! -e src/curl ]; then
                    make
                fi
                make install
                cp -r /usr/local/lib/libcurl* /usr/lib64/
            """.trimIndent()
        }
    }
}
