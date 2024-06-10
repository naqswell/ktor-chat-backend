package com.naqswell.features.auth.data

import com.naqswell.features.auth.data.dbo.UserDbo
import com.naqswell.features.auth.domain.model.UserModel

fun UserModel.toDbo() = UserDbo(
    username = username,
    email = email,
    passwordHash = passwordHash,
    salt = salt,
    refreshToken = refreshToken
)

/**
 * Need to update whole Mongo Entity without specifying all fields manually.
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