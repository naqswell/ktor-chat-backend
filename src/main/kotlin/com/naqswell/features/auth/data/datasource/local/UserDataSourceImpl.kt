package com.naqswell.features.auth.data.datasource.local

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.naqswell.db.dbo.UserDbo
import com.naqswell.features.auth.data.toDbo
import com.naqswell.features.auth.data.toDboWithNullId
import com.naqswell.features.auth.domain.model.UserModel
import kotlinx.coroutines.flow.firstOrNull

/**
 * UserAuth data source impl
 * This datasource class is responsible for database actions only.
 */
internal class UserDataSourceImpl(
    private val database: MongoDatabase
) : UserDataSource {

    private val users
        get() = database.getCollection<UserDbo>(USER_AUTH_COLLECTION)

    override suspend fun insertUser(userModel: UserModel): UserDbo? {
        val userDbo = userModel.toDbo()
        try {
            users.insertOne(userDbo)
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
            return null
        }
        return userDbo
    }

    override suspend fun updateUser(old: UserModel, new: UserModel): UserDbo? {
        try {
            val filter = Filters.eq(UserDbo::refreshToken.name, old.refreshToken)
            users.replaceOne(
                filter = filter,
                replacement = new.toDboWithNullId()
            )
        } catch (e: MongoException) {
            System.err.println("Unable to update user due to an error: $e")
            return null
        }
        return new.toDbo()
    }

    override suspend fun getUserByEmail(email: String): UserDbo? {
        return users
            .withDocumentClass<UserDbo>()
            .find(Filters.eq(UserDbo::email.name, email))
            .firstOrNull()
    }

    override suspend fun getUserByToken(token: String): UserDbo? {
        return users
            .withDocumentClass<UserDbo>()
            .find(Filters.eq(UserDbo::refreshToken.name, token))
            .firstOrNull()
    }

    private companion object {
        const val USER_AUTH_COLLECTION = "UserAuthCollection"
    }
}