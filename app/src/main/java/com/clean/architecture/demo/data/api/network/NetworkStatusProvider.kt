package com.clean.architecture.demo.data.api.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.clean.architecture.demo.domain.repository.NetworkStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkStatusProvider @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    val networkStatusFlow: Flow<NetworkStatus> = callbackFlow {

        fun sendCurrentStatus() {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            val isAvailable =
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            trySend(if (isAvailable) NetworkStatus.Available else NetworkStatus.Unavailable)
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                sendCurrentStatus()
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(request, callback)
        //send initial status
        sendCurrentStatus()
        //unregister when flow is closed
        awaitClose {
            cm.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged().conflate()
}