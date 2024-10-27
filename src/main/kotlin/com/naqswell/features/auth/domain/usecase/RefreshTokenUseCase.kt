package com.naqswell.features.auth.domain.usecase

import com.naqswell.common.Resource
import com.naqswell.config.AuthHocon
import com.naqswell.features.auth.domain.model.Auth
import com.naqswell.features.auth.domain.model.RefreshToken
import com.naqswell.features.auth.domain.repository.UserRepository
import com.naqswell.security.token.TokenClaim
import com.naqswell.security.token.TokenConfig
import com.naqswell.security.token.TokenService

internal class RefreshTokenUseCase(
    private val userRepository: UserRepository,
    private val tokenConfig: TokenConfig,
    private val tokenService: TokenService,
    private val authConf: AuthHocon,
) {
    suspend operator fun invoke(request: RefreshToken): Resource<Auth, ErrorData> {

        /** Check if refresh token is actual **/
        val savedUser = userRepository.getByToken(request.token)
            ?: run {
                return Resource.Error(ErrorData.NoSuchRefreshToken)
            }

        /** Get payload data **/
        val userPayload = tokenService.getPayload(
            token = request.token,
            config = tokenConfig,
            payload = authConf.jwt.payloads.user.userEmail
        ) ?: run {
            return Resource.Error(ErrorData.InvalidRefreshToken)
        }

        val userClaim = TokenClaim(
            name = authConf.jwt.payloads.user.userEmail,
            value = userPayload
        )

        val accessToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.accessToken, userClaim)
        val refreshToken = tokenService.generate(tokenConfig, tokenConfig.expirationSeconds.refreshToken, userClaim)

        /** Update refresh token in db **/
        userRepository.update(old = savedUser, new = savedUser.copy(refreshToken = refreshToken))

        return Resource.Success(
            Auth(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )
    }

    internal sealed class ErrorData(val message: String) {
        data object NoSuchRefreshToken : ErrorData("ERROR_NO_SUCH_REFRESH_TOKEN")
        data object InvalidRefreshToken : ErrorData("ERROR_INVALID_REFRESH_TOKEN")
    }
}