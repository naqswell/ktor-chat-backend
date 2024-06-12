package com.naqswell.features.auth.resource.usecase

import com.naqswell.common.ServerResponse
import com.naqswell.features.auth.data.toDto
import com.naqswell.features.auth.domain.repository.UserRepository
import com.naqswell.features.auth.resource.dto.request.LoginRequestDto
import com.naqswell.features.auth.resource.dto.response.AuthResponseDto
import com.naqswell.security.hashing.HashingService
import com.naqswell.security.hashing.SaltedHash
import com.naqswell.security.token.TokenClaim
import com.naqswell.security.token.TokenConfig
import com.naqswell.security.token.TokenService
import io.ktor.http.*


internal class LoginUseCase(
    private val repository: UserRepository,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig,
    private val hashingService: HashingService
) {
    suspend operator fun invoke(request: LoginRequestDto): ServerResponse<AuthResponseDto> {

        if (request.email.isNullOrEmpty())
            return ServerResponse.ErrorStatus(
                status = HttpStatusCode.UnprocessableEntity,
                message = "ERROR_MISSING_EMAIL"
            )

        if (request.password.isNullOrEmpty())
            return ServerResponse.ErrorStatus(
                status = HttpStatusCode.UnprocessableEntity,
                message = "ERROR_MISSING_PASSWORD"
            )

        val savedUser = repository.getUserByEmail(request.email)
            ?: run {
                return ServerResponse.ErrorStatus(
                    status = HttpStatusCode.UnprocessableEntity,
                    message = "ERROR_USER_NOT_FOUND"
                )
            }

        return if (
            hashingService.verify(
                password = request.password,
                expectedSaltedHash = SaltedHash(hash = savedUser.passwordHash, salt = savedUser.salt)
            )
        ) {
            val userClaim = TokenClaim(
                name = "userEmail",
                value = savedUser.email
            )

            val accessToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.accessToken, userClaim)
            val refreshToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.refreshToken, userClaim)
            ServerResponse.Data(AuthResponseDto(data = savedUser.toDto(), accessToken = accessToken, refreshToken = refreshToken))
        } else {
            ServerResponse.ErrorStatus(
                status = HttpStatusCode.UnprocessableEntity,
                message = "ERROR_INVALID_CREDENTIALS"
            )
        }
    }
}