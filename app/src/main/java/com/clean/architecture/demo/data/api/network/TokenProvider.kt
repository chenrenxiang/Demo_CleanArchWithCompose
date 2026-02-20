package com.clean.architecture.demo.data.api.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.clean.architecture.demo.app.ApplicationScope
import com.clean.architecture.demo.data.datastore.AppDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.Volatile


interface TokenProvider {
    fun getAccessToken(): String?
}

@Singleton
class DataStoreTokenProvider @Inject constructor(
    private val appDataStore: AppDataStore,
    @param:ApplicationScope private val coroutineScope: CoroutineScope
) : TokenProvider {
    @Volatile private var verifiedToken: String? = null
    @Volatile private var unverifiedToken: String? = null

    init {
        coroutineScope.launch {
            appDataStore.getUnverifiedToken().collect {
                unverifiedToken = it
            }
        }
        coroutineScope.launch {
            appDataStore.getToken().collect {
                verifiedToken = it
            }
        }
    }

    override fun getAccessToken(): String? {
        return verifiedToken ?: unverifiedToken
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenBindingModule {
    @Binds
    @Singleton
    abstract fun bindTokenProvider(
        tokenProvider: DataStoreTokenProvider
    ): TokenProvider
}