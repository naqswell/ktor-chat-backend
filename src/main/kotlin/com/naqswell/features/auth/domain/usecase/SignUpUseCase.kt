package com.naqswell.features.auth.domain.usecase

import com.naqswell.common.Resource
import com.naqswell.config.AuthHocon
import com.naqswell.features.auth.data.toDto
import com.naqswell.features.auth.data.toUserModel
import com.naqswell.features.auth.domain.model.Auth
import com.naqswell.features.auth.domain.model.Signup
import com.naqswell.features.auth.domain.repository.UserRepository
import com.naqswell.security.hashing.HashingService
import com.naqswell.security.token.TokenClaim
import com.naqswell.security.token.TokenConfig
import com.naqswell.security.token.TokenService

internal class SignUpUseCase(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig,
    private val hashingService: HashingService,
    private val config: AuthHocon
) {
    suspend operator fun invoke(request: Signup): Resource<Auth, ErrorData> {

        if (request.username.isEmpty() || request.email.isEmpty() || request.password.isEmpty())
            return Resource.Error(ErrorData.InvalidUsernameOrEmailOrPassword)

        // Check db for existing user by username, to check username is unique
        return when (userRepository.getByUsernameOrEmail(username = request.username, email = request.email)) {
            // Is new user
            null -> {
                val userClaim = TokenClaim(name = config.jwt.payloads.user.userEmail, value = request.email)

                val accessToken =
                    tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.accessToken, userClaim)
                val refreshToken =
                    tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.refreshToken, userClaim)

                val saltedHash = hashingService.generateSaltedHash(request.password)

                val userModel = request.toUserModel(
                    salt = saltedHash.salt,
                    hash = saltedHash.hash,
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )

                Resource.Success(
                    Auth(
                        data = userRepository.insert(userModel)?.toDto(),
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )
            }

            // User already exist
            else -> Resource.Error(ErrorData.UserAlreadyExist)
        }
    }

    internal sealed class ErrorData(val message: String) {
        data object InvalidUsernameOrEmailOrPassword : ErrorData("ERROR_INVALID_USERNAME_OR_EMAIL_OR_PASSWORD")
        data object UserAlreadyExist : ErrorData("ERROR_USER_ALREADY_EXIST")
    }
}