package com.naqswell.features.auth.domain.usecase

import com.naqswell.common.Resource
import com.naqswell.config.AuthHocon
import com.naqswell.features.auth.data.toDto
import com.naqswell.features.auth.domain.model.Auth
import com.naqswell.features.auth.domain.model.Login
import com.naqswell.features.auth.domain.repository.UserRepository
import com.naqswell.security.hashing.HashingService
import com.naqswell.security.hashing.SaltedHash
import com.naqswell.security.token.TokenClaim
import com.naqswell.security.token.TokenConfig
import com.naqswell.security.token.TokenService


internal class LoginUseCase(
    private val repository: UserRepository,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig,
    private val hashingService: HashingService,
    private val config: AuthHocon
) {
    suspend operator fun invoke(request: Login): Resource<Auth, ErrorData> {

        if (request.email.isEmpty())
            return Resource.Error(ErrorData.MissingEmail)

        if (request.password.isEmpty())
            return Resource.Error(ErrorData.MissingPassword)

        val savedUser = repository.getByEmail(request.email)
            ?: run {
                return Resource.Error(ErrorData.UserNotFound)
            }

        return if (
            hashingService.verify(
                password = request.password,
                expectedSaltedHash = SaltedHash(hash = savedUser.passwordHash, salt = savedUser.salt)
            )
        ) {
            val userClaim = TokenClaim(
                name = config.jwt.payloads.user.userEmail,
                value = savedUser.email
            )

            val accessToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.accessToken, userClaim)
            val refreshToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.refreshToken, userClaim)
            Resource.Success(
                Auth(
                    data = savedUser.toDto(),
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        } else {
            Resource.Error(ErrorData.InvalidCredentials)
        }
    }

    internal sealed class ErrorData(val message: String) {
        data object MissingEmail : ErrorData("ERROR_MISSING_EMAIL")
        data object MissingPassword : ErrorData("ERROR_MISSING_PASSWORD")
        data object UserNotFound : ErrorData("ERROR_USER_NOT_FOUND")
        data object InvalidCredentials : ErrorData("ERROR_INVALID_CREDENTIALS")
    }
}