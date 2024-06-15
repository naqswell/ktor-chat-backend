package com.naqswell.features.auth.data.repository

import com.naqswell.features.auth.data.datasource.local.UserDataSource
import com.naqswell.features.auth.data.toUser
import com.naqswell.features.auth.domain.model.UserModel
import com.naqswell.features.auth.domain.repository.UserRepository

internal class UserRepositoryImpl(
    private val datasource: UserDataSource,
): UserRepository {

    override suspend fun insert(userModel: UserModel): UserModel? {
        return datasource.insert(userModel)?.toUser()
    }

    override suspend fun update(old: UserModel, new: UserModel): UserModel? {
        return datasource.update(old, new)?.toUser()
    }

    override suspend fun getByEmail(email: String): UserModel? {
        return datasource.getByEmail(email)?.toUser()
    }

    override suspend fun getByUsername(username: String): UserModel? {
        return datasource.getByUsername(username)?.toUser()
    }

    override suspend fun getByUsernameOrEmail(username: String, email: String): UserModel? {
        return datasource.getByUsernameOrEmail(username, email)?.toUser()
    }

    override suspend fun getByToken(token: String): UserModel? {
        return datasource.getByToken(token)?.toUser()
    }
}