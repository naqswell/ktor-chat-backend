package com.naqswell.features.auth.domain.model

import com.naqswell.features.auth.resource.dto.UserModelDto

data class Auth(
    val accessToken: String,
    val refreshToken: String,
    val data: UserModelDto? = null,
)