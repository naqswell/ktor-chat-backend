package com.naqswell.di

import com.naqswell.config.AuthHocon
import com.naqswell.security.token.ExpirationMilliSecondsConfig
import com.naqswell.security.token.JwtTokenService
import com.naqswell.security.token.TokenConfig
import com.naqswell.security.token.TokenService
import org.koin.dsl.module

val securityModule = module {

    single<TokenConfig> {
        val conf = get<AuthHocon>().jwt
        TokenConfig(
            realm = conf.realm,
            issuer = conf.issuer,
            audience = conf.audience,
            secret = conf.secret,
            expirationSeconds = ExpirationMilliSecondsConfig(
                accessToken = conf.expirationMilliSeconds.accessToken,
                refreshToken = conf.expirationMilliSeconds.refreshToken
            )
        )
    }

    single<TokenService> { JwtTokenService() }
}