package com.clean.architecture.demo.ui.feature.account.register

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.clean.architecture.demo.app.Page
import com.clean.architecture.demo.common.State
import com.clean.architecture.demo.common.StateEventViewModel
import com.clean.architecture.demo.common.UiEvent
import com.clean.architecture.demo.common.removeSpaces
import com.clean.architecture.demo.domain.model.RegisterEntity
import com.clean.architecture.demo.domain.usecase.AuthUseCase
import com.clean.architecture.demo.domain.usecase.TokenUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    var registerState: State<Unit> = State.Idle,
    var isOnCooldown: Boolean = false,
    var isEmail: Boolean = false,
    val email: String = "",
    val name: String = "",
    val kana: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
) {
    fun registerEnable() = name.isNotBlank()
            && kana.isNotBlank()
            && password.isNotBlank()
            && passwordConfirm.isNotBlank()
            && email.isNotBlank()
            && !registerState.isLoading()
}

interface RegisterEvent {
    fun register()
    fun onNameInputted(name: String)
    fun onKanaNameInputted(kana: String)
    fun onEmailInputted(email: String)
    fun onPasswordInputted(password: String)
    fun onPasswordConfirmInputted(passwordConfirm: String)
    fun resetRegisterState()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenUseCase: TokenUseCase
) : StateEventViewModel<RegisterUiState>(RegisterUiState()), RegisterEvent {

    override fun register() {
        updateState { it.copy(registerState = State.Loading) }
        viewModelScope.launch {
            val reqRegister = RegisterEntity(
                email = uiState.value.email,
                name = uiState.value.name.removeSpaces(),
                kana = uiState.value.kana.removeSpaces(),
                password = uiState.value.password,
                passwordTwice = uiState.value.passwordConfirm,
            )
            val result = authUseCase.register(reqRegister)
            if (result.isSuccess()) {
                tokenUseCase.clearAllToken()
                tokenUseCase.saveUnverifiedToken(result.successData())
                sendEvent(UiEvent.Navigate(Page.Verify))
                updateState { it.copy(registerState = State.Success(Unit)) }
            } else {
                updateState { it.copy(registerState = State.Failure(result.errorMsg())) }
            }
        }
    }

    override fun onNameInputted(name: String) {
        updateState { it.copy(name = name) }
    }

    override fun onKanaNameInputted(kana: String) {
        updateState { it.copy(kana = kana) }
    }

    override fun onEmailInputted(email: String) {
        updateState { it.copy(email = email) }
    }

    override fun onPasswordInputted(password: String) {
        updateState { it.copy(password = password) }
    }

    override fun onPasswordConfirmInputted(passwordConfirm: String) {
        updateState { it.copy(passwordConfirm = passwordConfirm) }
    }

    override fun resetRegisterState() {
        updateState { it.copy(registerState = State.Idle) }
    }

}