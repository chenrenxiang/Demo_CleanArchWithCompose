package com.clean.architecture.demo.data.api

import com.clean.architecture.demo.data.api.interfaces.IAccountApis
import com.clean.architecture.demo.data.api.interfaces.IHomeApis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApisModule {

    @Provides
    @Singleton
    fun provideAccountApis(retrofit: Retrofit): IAccountApis =
        retrofit.create(IAccountApis::class.java)

    @Provides
    @Singleton
    fun provideMedicalApis(retrofit: Retrofit): IHomeApis =
        retrofit.create(IHomeApis::class.java)
}