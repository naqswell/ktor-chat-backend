package com.naqswell.features.auth.domain.repository

import com.naqswell.features.auth.domain.model.UserModel

internal interface UserRepository {
    suspend fun insert(userModel: UserModel): UserModel?
    suspend fun update(old: UserModel, new: UserModel): UserModel?
    suspend fun getByEmail(email: String): UserModel?
    suspend fun getByUsername(username: String): UserModel?
    suspend fun getByUsernameOrEmail(username: String, email: String): UserModel?
    suspend fun getByToken(token: String): UserModel?
}