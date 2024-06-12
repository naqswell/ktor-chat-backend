package com.naqswell.di

import com.mongodb.ConnectionString
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.naqswell.config.DatabaseHocon
import org.koin.dsl.module

val databaseModule = module {

    single {
        val conf = get<DatabaseHocon>()
        MongoClient.create(ConnectionString(conf.mongo.uri))
    }

    single {
        val conf = get<DatabaseHocon>()
        get<MongoClient>().getDatabase(conf.name)
    }
}