package com.naqswell.features.auth.resource.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String? = null,
    val password: String? = null
)