package com.naqswell.features.auth.resource

import com.naqswell.common.Resource
import com.naqswell.features.auth.resource.dto.RefreshTokenDto
import com.naqswell.features.auth.resource.dto.LoginDto
import com.naqswell.features.auth.resource.dto.SignupDto
import com.naqswell.features.auth.domain.usecase.LoginUseCase
import com.naqswell.features.auth.domain.usecase.LoginUseCase.ErrorData as LoginErrorData
import com.naqswell.features.auth.domain.usecase.RefreshTokenUseCase
import com.naqswell.features.auth.domain.usecase.RefreshTokenUseCase.ErrorData as RefreshTokenErrorData
import com.naqswell.features.auth.domain.usecase.SignUpUseCase
import com.naqswell.features.auth.domain.usecase.SignUpUseCase.ErrorData as SignUpErrorData
import io.ktor.http.HttpStatusCode
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
            val request = call.receive<SignupDto>()

            when (val response = useCase(request = request.toDomain())) {
                is Resource.Success -> call.respond(response.data.toDto())
                is Resource.Error -> {
                    val status = when (response.errorData) {
                        SignUpErrorData.InvalidUsernameOrEmailOrPassword -> HttpStatusCode.UnprocessableEntity
                        SignUpErrorData.UserAlreadyExist -> HttpStatusCode.Conflict
                    }
                    call.respond(status = status, message = response.errorData.message)
                }
            }
        }
    }
}


fun Application.loginEndpoint() { //todo: revert to Routing extension after new koin fix
    val useCase: LoginUseCase by inject<LoginUseCase>()

    routing {
        post(ENDPOINT_LOGIN) {
            val request = call.receive<LoginDto>()

            when (val response = useCase(request = request.toDomain())) {
                is Resource.Success -> call.respond(response.data.toDto())
                is Resource.Error -> {
                    val status = when (response.errorData) {
                        LoginErrorData.InvalidCredentials,
                        LoginErrorData.MissingEmail,
                        LoginErrorData.MissingPassword,
                        LoginErrorData.UserNotFound -> HttpStatusCode.UnprocessableEntity
                    }
                    call.respond(status = status, message = response.errorData.message)
                }
            }
        }
    }
}

fun Application.refreshTokenEndpoint() { //todo: revert to Routing extension after new koin fix
    val userCase: RefreshTokenUseCase by inject<RefreshTokenUseCase>()

    routing {
        post(ENDPOINT_REFRESH) {
            val refreshToken = call.receive<RefreshTokenDto>()

            when (val response = userCase(request = refreshToken.toDomain())) {
                is Resource.Success -> call.respond(response.data.toDto())
                is Resource.Error -> {
                    val status = when(response.errorData) {
                        RefreshTokenErrorData.InvalidRefreshToken,
                        RefreshTokenErrorData.NoSuchRefreshToken -> HttpStatusCode.Forbidden
                    }
                    call.respond(status = status, message = response.errorData.message)
                }
            }
        }
    }
}