package com.naqswell.di

import com.naqswell.config.AuthHocon
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.koin.dsl.module

val hoconsModule = module {

    single<AuthHocon> {
        ConfigLoaderBuilder.default()
            .addResourceSource("/auth.conf")
            .build()
            .loadConfigOrThrow<AuthHocon>()
    }

}