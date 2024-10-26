package com.naqswell.features.auth.resource

import com.naqswell.common.ServerResponse
import com.naqswell.features.auth.resource.dto.RefreshTokenDto
import com.naqswell.features.auth.resource.dto.request.LoginRequestDto
import com.naqswell.features.auth.resource.dto.request.SignupRequestDto
import com.naqswell.features.auth.resource.usecase.LoginUseCase
import com.naqswell.features.auth.resource.usecase.RefreshTokenUseCase
import com.naqswell.features.auth.resource.usecase.SignUpUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal const val ENDPOINT_SIGNUP = "/auth/signup"
internal const val ENDPOINT_LOGIN = "/auth/login"
internal const val ENDPOINT_REFRESH = "/auth/refreshToken"

fun Application.signupEndpoint() { //todo: revert to Routing extension after new koin fix
    val useCase: SignUpUseCase by inject<SignUpUseCase>()

    routing {
        post(ENDPOINT_SIGNUP) {
            val request = call.receive<SignupRequestDto>()

            when (val response = useCase(request = request)) {
                is ServerResponse.Data -> call.respond(response.data)
                is ServerResponse.ErrorStatus -> call.respond(status = response.status, message = response.message)
            }
        }
    }
}


fun Application.loginEndpoint() { //todo: revert to Routing extension after new koin fix
    val useCase: LoginUseCase by inject<LoginUseCase>()

    routing {
        post(ENDPOINT_LOGIN) {
            val request = call.receive<LoginRequestDto>()

            when (val response = useCase(request = request)) {
                is ServerResponse.Data -> call.respond(response.data)
                is ServerResponse.ErrorStatus -> call.respond(status = response.status, message = response.message)
            }
        }
    }
}

fun Application.refreshTokenEndpoint() { //todo: revert to Routing extension after new koin fix
    val userCase: RefreshTokenUseCase by inject<RefreshTokenUseCase>()

    routing {
        post(ENDPOINT_REFRESH) {
            val refreshToken = call.receive<RefreshTokenDto>()

            when (val response = userCase(request = refreshToken)) {
                is ServerResponse.Data -> call.respond(response.data)
                is ServerResponse.ErrorStatus -> call.respond(status = response.status, message = response.message)
            }
        }
    }
}