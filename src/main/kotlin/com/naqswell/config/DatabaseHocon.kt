package com.naqswell.config

data class DatabaseHocon (
    val name: String,
    val mongo: MongoHocon
)

data class MongoHocon(
    val uri: String,
)