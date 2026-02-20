package com.clean.architecture.demo.ui.feature.account.start

import androidx.lifecycle.viewModelScope
import com.clean.architecture.demo.common.State
import com.clean.architecture.demo.common.StateEventViewModel
import com.clean.architecture.demo.domain.model.StartPageState
import com.clean.architecture.demo.domain.usecase.AppStartStateUseCase
import com.clean.architecture.demo.domain.usecase.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class StartUiState(
    val pageState: StartPageState = StartPageState.FirstIn,
    val agreementDisagreed: Boolean = false,
    val loadingState: State<Unit> = State.Idle
)

@HiltViewModel
class StartViewModel @Inject constructor(
    private val startStateUseCase: AppStartStateUseCase,
    private val settingsUseCase: SettingsUseCase
) : StateEventViewModel<StartUiState>(StartUiState()) {

    init {
        observeAccountState()
    }

    fun getAgreementUrl() = "http://exmaple.agreement.html"

    fun checkAgreement() {
        viewModelScope.launch {
            settingsUseCase.checkAgreement()
        }
    }

    private fun observeAccountState() {
        viewModelScope.launch {
            startStateUseCase.getAppStartPageState().collect { state ->
                updateState { it.copy(pageState = state) }
            }
        }
    }

    fun showAgreementDialog() {
        updateState { it.copy(pageState = StartPageState.Agreement) }
    }

    fun disagreeAgreement() {
        updateState { it.copy(agreementDisagreed = true) }
    }

    fun resetStartState() {
        updateState { it.copy(
            pageState = StartPageState.FirstIn,
            agreementDisagreed = false
        ) }
    }


}