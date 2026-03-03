package com.clean.architecture.demo.domain.repository

import kotlinx.coroutines.flow.Flow


enum class NetworkStatus {
    Available, Unavailable,
}

interface NetworkRepository {
    val networkStatus: Flow<NetworkStatus>
}

