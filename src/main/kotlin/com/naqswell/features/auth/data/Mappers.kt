package com.naqswell.features.auth.data

import com.naqswell.db.dbo.UserDbo
import com.naqswell.features.auth.domain.model.UserModel
import com.naqswell.features.auth.resource.dto.request.SignupRequestDto
import com.naqswell.features.auth.resource.dto.response.UserModelDto

fun SignupRequestDto.toUserModel(
    salt: String,
    hash: String,
    accessToken: String,
    refreshToken: String,
) = UserModel(
    username = username,
    email = email,
    passwordHash = hash,
    salt = salt,
    accessToken = accessToken,
    refreshToken = refreshToken
)

fun UserModel.toDto() = UserModelDto(
    username = username,
    email = email,
    avatar = avatar,
)

fun UserModel.toDbo() = UserDbo(
    username = username,
    email = email,
    passwordHash = passwordHash,
    salt = salt,
    refreshToken = refreshToken
)

/**
 * This is need to update whole Mongo Entity without specifying all fields manually.
 * Mongo cant update entity if ID is different, so it should be null for the object that acts as a replacement.
 */
fun UserModel.toDboWithNullId() = UserDbo(
    id = null,
    username = username,
    email = email,
    passwordHash = passwordHash,
    salt = salt,
    refreshToken = refreshToken
)

fun UserDbo.toUser() = UserModel(
    username = username,
    email = email,
    passwordHash = passwordHash,
    salt = salt,
    refreshToken = refreshToken,
    accessToken = null,
    avatar = avatar,
)