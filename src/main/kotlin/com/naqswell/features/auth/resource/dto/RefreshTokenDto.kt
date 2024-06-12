package com.naqswell.features.auth.resource.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto(
    val token: String
)