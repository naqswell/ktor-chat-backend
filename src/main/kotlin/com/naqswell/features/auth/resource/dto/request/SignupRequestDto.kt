package com.naqswell.features.auth.resource.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(
    val username: String,
    val email: String,
    val password: String
)