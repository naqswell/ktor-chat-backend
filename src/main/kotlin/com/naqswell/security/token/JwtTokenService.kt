package com.naqswell.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*


class JwtTokenService : TokenService {

    private val signAlgorithm: (secret: String) -> Algorithm = {
        Algorithm.HMAC256(it)
    }

    override fun generate(config: TokenConfig, expirationSeconds: Long, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationSeconds))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(signAlgorithm(config.secret))
    }

    override fun getPayload(token: String, config: TokenConfig, payload: String): String? {
        return try {
            val jwt = JWT.require(signAlgorithm(config.secret))
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .build()
                .verify(token)

            return jwt.getClaim(payload).asString()
        } catch (e: JWTVerificationException) {
            println("ERROR while verifying JWT || $e")
            null
        }
    }
}