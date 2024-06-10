package com.naqswell.features.auth.domain.model

data class UserModel(
    val username: String,
    val email: String,

    val passwordHash: String,
    val salt: String,

    val accessToken: String? = null,
    val refreshToken: String,

    val avatar: String? = null,
)