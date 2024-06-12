package com.naqswell.plugins

import com.naqswell.features.auth.resource.loginEndpoint
import com.naqswell.features.auth.resource.refreshTokenEndpoint
import com.naqswell.features.auth.resource.signupEndpoint
import com.naqswell.features.test.resource.testEmailPayloadEndpoint
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    install(Routing) {

        signupEndpoint()
        loginEndpoint()
        refreshTokenEndpoint()

        authenticate("auth-jwt") {
            testEmailPayloadEndpoint()
        }

    }

}
