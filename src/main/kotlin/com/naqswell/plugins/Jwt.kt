package com.naqswell.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.naqswell.config.AuthHocon
import com.naqswell.config.JwtHocon
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.get

fun Application.configureJwt() {
    val jwt: JwtHocon = get<AuthHocon>().jwt

    authentication {
        jwt("auth-jwt") {

            realm = jwt.realm

            verifier(
                JWT.require(Algorithm.HMAC256(jwt.secret))
                    .withAudience(jwt.audience)
                    .withIssuer(jwt.issuer)
                    .build()
            )

            validate { credential ->
                if (credential.payload.audience.contains(jwt.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }

            challenge { scheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token to access ${HttpHeaders.WWWAuthenticate} $scheme realm=\"$realm\" is either invalid or expired."
                )
            }
        }
    }
}