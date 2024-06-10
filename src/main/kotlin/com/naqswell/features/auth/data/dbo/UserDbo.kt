package com.naqswell.features.auth.data.dbo

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class UserDbo(
    /**
     * Nullable is need to update whole Mongo Entity without specifying all fields manually.
     * Mongo cant update entity if ID is different, so it should be null for the object that acts as a replacement.
     */
    @BsonId val id: String? = ObjectId().toString(),
    val username: String,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val refreshToken: String,
    val avatar: String? = null,
)