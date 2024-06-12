package com.naqswell.di

import com.naqswell.config.AuthHocon
import com.naqswell.config.DatabaseHocon
import com.naqswell.config.HashingHocon
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.koin.dsl.module

val hoconsModule = module {

    single<HashingHocon> {
        ConfigLoaderBuilder.default()
            .addResourceSource("/hashing.conf")
            .build()
            .loadConfigOrThrow<HashingHocon>()
    }

    single<DatabaseHocon> {
        ConfigLoaderBuilder.default()
            .addResourceSource("/database.conf")
            .build()
            .loadConfigOrThrow<DatabaseHocon>()
    }

    single<AuthHocon> {
        ConfigLoaderBuilder.default()
            .addResourceSource("/auth.conf")
            .build()
            .loadConfigOrThrow<AuthHocon>()
    }

}