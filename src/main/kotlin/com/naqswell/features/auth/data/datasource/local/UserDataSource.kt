package com.naqswell.features.auth.data.datasource.local

import com.naqswell.db.dbo.UserDbo
import com.naqswell.features.auth.domain.model.UserModel

internal interface UserDataSource {
    suspend fun insert(userModel: UserModel): UserDbo?
    suspend fun update(old: UserModel, new: UserModel): UserDbo?
    suspend fun getByEmail(email: String): UserDbo?
    suspend fun getByUsername(username: String): UserDbo?
    suspend fun getByUsernameOrEmail(username: String, email: String): UserDbo?
    suspend fun getByToken(token: String): UserDbo?
}