package com.naqswell.features.test.resource

import com.naqswell.config.AuthHocon
import com.naqswell.features.test.resource.dto.response.TestResponseDto
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

const val ENDPOINT_TEST_PAYLOAD = "/test/auth"

fun Application.testEmailPayloadEndpoint() {
    val config: AuthHocon by inject<AuthHocon>()

    routing {
        authenticate("auth-jwt") {
            post(ENDPOINT_TEST_PAYLOAD) {//todo: revert to Routing extension after new koin fix
                val getPayload: (String) -> String? = { payload: String ->
                    call.principal<JWTPrincipal>()?.payload?.getClaim(payload)?.asString()
                }

                val payload = config.jwt.payloads.user.userEmail
                val email = getPayload(payload)

                val response = TestResponseDto(
                    data = "$email"
                )

                call.respond(response)
            }
        }
    }
}