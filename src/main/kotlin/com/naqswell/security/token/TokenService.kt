package com.naqswell.security.token

interface TokenService {
    fun generate(config: TokenConfig, expirationSeconds: Long, vararg claims: TokenClaim): String
    fun getPayload(token: String, config: TokenConfig, payload: String): String?
}