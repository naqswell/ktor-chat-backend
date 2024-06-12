package com.naqswell

import com.naqswell.config.DeploymentHocon
import com.naqswell.plugins.configureDi
import com.naqswell.plugins.configureRouting
import com.naqswell.plugins.configureSerialization
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val deployment = ConfigLoaderBuilder.default()
        .addResourceSource("/deployment.conf")
        .build()
        .loadConfigOrThrow<DeploymentHocon>()

    embeddedServer(Netty, port = deployment.port) {
        configureDi()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
