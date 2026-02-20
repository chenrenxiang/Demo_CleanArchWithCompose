package com.clean.architecture.demo.ui.feature.account.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.clean.architecture.demo.common.State
import com.clean.architecture.demo.common.StateEventViewModel
import com.clean.architecture.demo.common.log
import com.clean.architecture.demo.domain.usecase.AuthUseCase
import com.clean.architecture.demo.domain.usecase.TokenUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val require2FA: Boolean = true,
    val loginState: State<String?> = State.Idle
) {

    fun loginEnabled(): Boolean {
        return !email.isBlank() && !password.isBlank() && loginState !is State.Loading
    }
}

interface LoginPageEvent {
    fun login()
    fun onEmailChanged(email: String)
    fun onPasswordChanged(password: String)
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenUseCase: TokenUseCase
): StateEventViewModel<LoginUiState>(LoginUiState()), LoginPageEvent {

    override fun login() {
        updateState { it.copy(loginState = State.Loading) }
        viewModelScope.launch {
            val response = authUseCase.login(uiState.value.email, uiState.value.password)
            if(response.isSuccess()) {
                val loginResult = response.successData()
                if(loginResult.requiresTwoFactor == true) {
                    tokenUseCase.clearAllToken()
                    tokenUseCase.saveUnverifiedToken(loginResult.token)
                    log("saveUnverifiedToken", loginResult.token)
                } else {
                    tokenUseCase.saveVerifiedToken(loginResult.token)
                    log("saveToken", loginResult.token)
                }
                updateState {
                    it.copy(
                        loginState = State.Success(loginResult.message),
                        require2FA = loginResult.requiresTwoFactor == true
                    )
                }
            } else {
                updateState { it.copy(loginState = State.Failure(response.errorMsg())) }
            }
        }
    }

    override fun onEmailChanged(email: String) {
        updateState { it.copy(email = email) }
    }

    override fun onPasswordChanged(password: String) {
        updateState { it.copy(password = password) }
    }

    fun resetLoginState() {
        updateState { it.copy(loginState = State.Idle) }
    }


}