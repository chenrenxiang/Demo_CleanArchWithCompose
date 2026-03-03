package com.clean.architecture.demo.app

import androidx.lifecycle.viewModelScope
import com.clean.architecture.demo.common.StateEventViewModel
import com.clean.architecture.demo.common.UiEvent
import com.clean.architecture.demo.domain.repository.NetworkRepository
import com.clean.architecture.demo.domain.repository.NetworkStatus
import com.clean.architecture.demo.domain.usecase.AccountActionUseCase
import com.clean.architecture.demo.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AppUiState(
    val networkStatus: NetworkStatus = NetworkStatus.Available,
)

@HiltViewModel
class AppViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val authUseCase: AuthUseCase,
    private val accountActionUseCase: AccountActionUseCase,
) : StateEventViewModel<AppUiState>(AppUiState()) {

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        networkRepository.networkStatus.onEach { status ->
            updateState { it.copy(networkStatus = status) }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            val result = authUseCase.logout()
            if(result.isSuccess()) {
                sendEvent(UiEvent.Navigate(Page.Start))
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val result = accountActionUseCase.deleteAccount()
            if(result.isSuccess()) {
                sendEvent(UiEvent.Navigate(Page.Start))
            } else {
                result.errorMsg()?.let { sendEvent(UiEvent.Toast(it)) }
            }
        }
    }
}