package com.clean.architecture.demo.ui.feature.account.verify

import androidx.lifecycle.viewModelScope
import com.clean.architecture.demo.common.State
import com.clean.architecture.demo.common.StateEventViewModel
import com.clean.architecture.demo.common.UiEvent
import com.clean.architecture.demo.domain.usecase.AuthUseCase
import com.clean.architecture.demo.domain.usecase.TokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VerifyUiState(
    val useBiometricAuth: Boolean = false,
    val verifyState: State<String?> = State.Idle,
)

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenUseCase: TokenUseCase,
) : StateEventViewModel<VerifyUiState>(VerifyUiState()) {

    init {
        viewModelScope.launch {
            authUseCase.useBiometricAuth().collect { use ->
                updateState { it.copy(useBiometricAuth = use) }
            }
        }
    }

    fun verify(code: String, useBiometricAuth: Boolean) {
        updateState { it.copy(verifyState = State.Loading) }
        viewModelScope.launch {
            val resp = authUseCase.verifyCode(code = code)
            if(resp.isSuccess()) {
                val verifyResult = resp.successData()
                verifyResult.message?.let { sendEvent(UiEvent.Toast(it)) }
                tokenUseCase.saveVerifiedToken(verifyResult.token)
                authUseCase.saveUseBiometricAuth(useBiometricAuth)
                updateState {
                    it.copy(
                        verifyState = State.Success(verifyResult.message),
                        useBiometricAuth = useBiometricAuth
                    )
                }
            } else {
                updateState { it.copy(verifyState = State.Failure(resp.errorMsg())) }
            }
        }
    }

    fun resetVerifyState() {
        updateState { it.copy(verifyState = State.Idle) }
    }

}