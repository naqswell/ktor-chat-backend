package com.naqswell.features.auth.domain.repository

import com.naqswell.features.auth.domain.model.UserModel

internal interface UserRepository {
    suspend fun insertUser(userModel: UserModel): UserModel?
    suspend fun updateUser(old: UserModel, new: UserModel): UserModel?
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserByToken(token: String): UserModel?
}