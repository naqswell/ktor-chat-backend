package com.naqswell.features.auth.resource

import com.naqswell.features.auth.domain.model.Auth
import com.naqswell.features.auth.domain.model.Login
import com.naqswell.features.auth.domain.model.RefreshToken
import com.naqswell.features.auth.domain.model.Signup
import com.naqswell.features.auth.resource.dto.AuthDto
import com.naqswell.features.auth.resource.dto.LoginDto
import com.naqswell.features.auth.resource.dto.RefreshTokenDto
import com.naqswell.features.auth.resource.dto.SignupDto

fun LoginDto.toDomain(): Login {
    return Login(
        email = email,
        password = password
    )
}

fun Auth.toDto(): AuthDto {
    return AuthDto(
        accessToken = accessToken,
        refreshToken = refreshToken,
        data = data
    )
}

fun RefreshTokenDto.toDomain(): RefreshToken {
    return RefreshToken(
        token = token
    )
}

fun SignupDto.toDomain(): Signup {
    return Signup(
        username = username,
        email = email,
        password = password
    )
}