package com.naqswell.plugins

import com.naqswell.features.auth.resource.loginEndpoint
import com.naqswell.features.auth.resource.refreshTokenEndpoint
import com.naqswell.features.auth.resource.signupEndpoint
import com.naqswell.features.test.resource.testEmailPayloadEndpoint
import io.ktor.server.application.*

fun Application.configureRouting() {
    signupEndpoint()
    loginEndpoint()
    refreshTokenEndpoint()

    testEmailPayloadEndpoint()
}
