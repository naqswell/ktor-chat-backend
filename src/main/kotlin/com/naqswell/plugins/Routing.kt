package com.naqswell.plugins

import com.naqswell.features.auth.resource.loginEndpoint
import com.naqswell.features.auth.resource.signupEndpoint
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    install(Routing) {

        signupEndpoint()
        loginEndpoint()

    }

}
