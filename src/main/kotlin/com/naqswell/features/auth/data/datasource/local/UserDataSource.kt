package com.naqswell.features.auth.data.datasource.local

import com.naqswell.features.auth.data.dbo.UserDbo
import com.naqswell.features.auth.domain.model.UserModel

internal interface UserDataSource {
    suspend fun insertUser(userModel: UserModel): UserDbo?
    suspend fun updateUser(old: UserModel, new: UserModel): UserDbo?
    suspend fun getUserByEmail(email: String): UserDbo?
    suspend fun getUserByToken(token: String): UserDbo?
}