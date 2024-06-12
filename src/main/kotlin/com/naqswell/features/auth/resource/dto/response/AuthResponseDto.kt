package com.naqswell.features.auth.resource.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val data: UserModelDto? = null,
)