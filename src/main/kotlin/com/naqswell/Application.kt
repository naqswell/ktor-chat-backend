package com.naqswell

import com.naqswell.config.DeploymentHocon
import com.naqswell.plugins.configureRouting
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
        configureRouting()
    }.start(wait = true)
}
