package com.naqswell.plugins

import com.naqswell.di.hoconsModule
import com.naqswell.di.securityModule
import com.naqswell.features.auth.di.authModule
import com.naqswell.di.databaseModule
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

fun configureDi() {
    startKoin {
        slf4jLogger()
        modules(
            hoconsModule,
            securityModule,
            databaseModule,
            authModule
        )
    }
}