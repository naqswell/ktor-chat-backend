package com.naqswell.features.auth.resource.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupDto(
    val username: String,
    val email: String,
    val password: String
)