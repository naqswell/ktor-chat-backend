package com.naqswell.config

data class AuthHocon(
    val jwt: JwtHocon,
)

data class JwtHocon(
    val secret: String,
    val audience: String,
    val domain: String,
    val realm: String,
    val issuer: String,
    val verify: Boolean,
    val authScheme: String,
    val expirationMilliSeconds: ExpirationMilliSecondsHocon,

    val payloads: PayloadsHocon,
)


data class ExpirationMilliSecondsHocon(
    val accessToken: Long,
    val refreshToken: Long
)

data class PayloadsHocon(
    val user: UserHocon
)

data class UserHocon(val userEmail: String)


