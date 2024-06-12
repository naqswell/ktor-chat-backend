package com.naqswell.features.auth.di

import com.naqswell.features.auth.data.datasource.local.UserDataSource
import com.naqswell.features.auth.data.datasource.local.UserDataSourceImpl
import com.naqswell.features.auth.data.repository.UserRepositoryImpl
import com.naqswell.features.auth.domain.repository.UserRepository
import com.naqswell.features.auth.resource.usecase.LoginUseCase
import com.naqswell.features.auth.resource.usecase.RefreshTokenUseCase
import com.naqswell.features.auth.resource.usecase.SignUpUseCase
import org.koin.dsl.module

val authModule = module {
    single<UserDataSource> { UserDataSourceImpl(get()) }

    single<UserRepository> { UserRepositoryImpl(get()) }

    factory<SignUpUseCase> { SignUpUseCase(get(), get(), get(), get()) }

    factory<LoginUseCase> { LoginUseCase(get(), get(), get(), get()) }

    factory<RefreshTokenUseCase> { RefreshTokenUseCase(get(), get(), get(), get()) }
}