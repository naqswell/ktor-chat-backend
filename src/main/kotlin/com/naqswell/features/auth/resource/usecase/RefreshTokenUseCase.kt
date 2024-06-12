package com.naqswell.features.auth.resource.usecase

import com.naqswell.common.ServerResponse
import com.naqswell.config.AuthHocon
import com.naqswell.features.auth.domain.repository.UserRepository
import com.naqswell.features.auth.resource.dto.RefreshTokenDto
import com.naqswell.features.auth.resource.dto.response.AuthResponseDto
import com.naqswell.security.token.TokenClaim
import com.naqswell.security.token.TokenConfig
import com.naqswell.security.token.TokenService
import io.ktor.http.*

internal class RefreshTokenUseCase(
    private val userRepository: UserRepository,
    private val tokenConfig: TokenConfig,
    private val tokenService: TokenService,
    private val authConf: AuthHocon,
) {
    suspend operator fun invoke(request: RefreshTokenDto): ServerResponse<AuthResponseDto> {

        /** Check if refresh token is actual **/
        val savedUser = userRepository.getUserByToken(request.token)
            ?: run {
                return ServerResponse.ErrorStatus(
                    status = HttpStatusCode.Forbidden,
                    message = "ERROR_NO_SUCH_REFRESH_TOKEN"
                )
            }

        /** Get payload data **/
        val userPayload = tokenService.getPayload(
            token = request.token,
            config = tokenConfig,
            payload = authConf.jwt.payloads.user.userEmail
        ) ?: run {
            return ServerResponse.ErrorStatus(
                status = HttpStatusCode.Forbidden,
                message = "ERROR_INVALID_REFRESH_TOKEN"
            )
        }

        val userClaim = TokenClaim(
            name = authConf.jwt.payloads.user.userEmail,
            value = userPayload
        )

        val accessToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.accessToken, userClaim)
        val refreshToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.refreshToken, userClaim)

        /** Update refresh token in db **/
        userRepository.updateUser(old = savedUser, new = savedUser.copy(refreshToken = refreshToken))

        return ServerResponse.Data(
            AuthResponseDto(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )

    }
}