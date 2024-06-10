package com.naqswell.features.auth.data.repository

import com.naqswell.features.auth.data.datasource.local.UserDataSource
import com.naqswell.features.auth.data.toUser
import com.naqswell.features.auth.domain.model.UserModel
import com.naqswell.features.auth.domain.repository.UserRepository

internal class UserRepositoryImpl(
    private val datasource: UserDataSource,
): UserRepository {

    override suspend fun insertUser(userModel: UserModel): UserModel? {
        return datasource.insertUser(userModel)?.toUser()
    }

    override suspend fun updateUser(old: UserModel, new: UserModel): UserModel? {
        return datasource.updateUser(old, new)?.toUser()
    }

    override suspend fun getUserByEmail(email: String): UserModel? {
        return datasource.getUserByEmail(email)?.toUser()
    }

    override suspend fun getUserByToken(token: String): UserModel? {
        return datasource.getUserByToken(token)?.toUser()
    }
}