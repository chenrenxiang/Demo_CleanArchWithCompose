package com.clean.architecture.demo.data.repository


import com.clean.architecture.demo.data.api.network.NetworkStatusProvider
import com.clean.architecture.demo.domain.repository.NetworkRepository
import com.clean.architecture.demo.domain.repository.NetworkStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImp @Inject constructor(
    networkStatusProvider: NetworkStatusProvider
): NetworkRepository {

    override val networkStatus: Flow<NetworkStatus> = networkStatusProvider.networkStatusFlow

}