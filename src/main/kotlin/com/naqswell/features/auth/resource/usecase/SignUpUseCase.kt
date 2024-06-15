package com.naqswell.features.auth.resource.usecase

import com.naqswell.common.ServerResponse
import com.naqswell.features.auth.data.toDto
import com.naqswell.features.auth.data.toUserModel
import com.naqswell.features.auth.domain.repository.UserRepository
import com.naqswell.features.auth.resource.dto.request.SignupRequestDto
import com.naqswell.features.auth.resource.dto.response.AuthResponseDto
import com.naqswell.security.hashing.HashingService
import com.naqswell.security.token.TokenClaim
import com.naqswell.security.token.TokenConfig
import com.naqswell.security.token.TokenService
import io.ktor.http.*

internal class SignUpUseCase(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig,
    private val hashingService: HashingService
) {
    suspend operator fun invoke(request: SignupRequestDto): ServerResponse<AuthResponseDto> {

        if (request.username.isEmpty() || request.email.isEmpty() || request.password.isEmpty())
            return ServerResponse.ErrorStatus(
                status = HttpStatusCode.UnprocessableEntity,
                message = "Invalid username, email or password"
            )


        // Check db for existing user by username, to check username is unique
        return when (userRepository.getByUsernameOrEmail(username = request.username, email = request.email)) {
            // Is new user
            null -> {
                val userClaim = TokenClaim(name = "userEmail", value = request.email)

                val accessToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.accessToken, userClaim)
                val refreshToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.refreshToken, userClaim)

                val saltedHash = hashingService.generateSaltedHash(request.password)

                val userModel = request.toUserModel(
                    salt = saltedHash.salt,
                    hash = saltedHash.hash,
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )

                ServerResponse.Data(
                    AuthResponseDto(
                        data = userRepository.insert(userModel)?.toDto(),
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )
            }

            // User already exist
            else -> ServerResponse.ErrorStatus(status = HttpStatusCode.Conflict, message = "User already exist")
        }
    }
}