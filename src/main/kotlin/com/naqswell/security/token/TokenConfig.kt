package com.naqswell.security.token

data class TokenConfig(
    val realm: String,
    val issuer: String,
    val audience: String,
    val secret: String,
    val expirationSeconds: ExpirationMilliSecondsConfig
)

data class ExpirationMilliSecondsConfig(
    val accessToken: Long,
    val refreshToken: Long
)