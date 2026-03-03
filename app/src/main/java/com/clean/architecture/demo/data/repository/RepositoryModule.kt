package com.clean.architecture.demo.data.repository

import com.clean.architecture.demo.domain.repository.AccountRepository
import com.clean.architecture.demo.domain.repository.AuthRepository
import com.clean.architecture.demo.domain.repository.HomeRepository
import com.clean.architecture.demo.domain.repository.NetworkRepository
import com.clean.architecture.demo.domain.repository.SettingsRepository
import com.clean.architecture.demo.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//all the repository implementations should be added here

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindAccountRepository(
        accountRepositoryImp: AccountRepositoryImp
    ): AccountRepository

    @Binds @Singleton
    abstract fun bindTokenRepository(
        tokenRepositoryImp: TokenRepositoryImp
    ): TokenRepository

    @Binds @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImp: SettingsRepositoryImp
    ): SettingsRepository

    @Binds @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImp: AuthRepositoryImp
    ): AuthRepository

    @Binds @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImp: HomeRepositoryImp
    ): HomeRepository

    @Binds @Singleton
    abstract fun bindNetworkRepository(
        networkRepositoryImp: NetworkRepositoryImp
    ): NetworkRepository
}
