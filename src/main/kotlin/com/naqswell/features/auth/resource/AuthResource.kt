package com.naqswell.features.auth.resource

import com.naqswell.features.auth.resource.dto.request.LoginRequestDto
import com.naqswell.features.auth.resource.dto.request.SignupRequestDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val ENDPOINT_SIGNUP = "/auth/signup"
const val ENDPOINT_LOGIN = "/auth/login"

fun Route.signupEndpoint() {

    post(ENDPOINT_SIGNUP) {
        val request = call.receive<SignupRequestDto>()
        call.respond(status = HttpStatusCode.OK, message = "SIGNUP")
    }
}


fun Route.loginEndpoint() {

    post(ENDPOINT_LOGIN) {
        val request = call.receive<LoginRequestDto>()
        call.respond(status = HttpStatusCode.OK, message = "LOGIN")
    }
}