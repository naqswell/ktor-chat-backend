package com.naqswell.features.auth.resource.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserModelDto(
    val username: String,
    val email: String,
    val avatar: String? = null,
)