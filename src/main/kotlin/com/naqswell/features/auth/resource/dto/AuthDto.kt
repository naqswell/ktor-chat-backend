package com.naqswell.features.auth.resource.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val accessToken: String,
    val refreshToken: String,
    val data: UserModelDto? = null,
)