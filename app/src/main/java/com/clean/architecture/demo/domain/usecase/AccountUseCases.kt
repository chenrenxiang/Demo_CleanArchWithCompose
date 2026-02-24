package com.clean.architecture.demo.domain.usecase

import com.clean.architecture.demo.domain.repository.AccountRepository
import com.clean.architecture.demo.domain.repository.AuthRepository
import com.clean.architecture.demo.domain.repository.SettingsRepository
import com.clean.architecture.demo.domain.repository.TokenRepository
import com.clean.architecture.demo.domain.model.ApiResult
import com.clean.architecture.demo.domain.model.LoginResult
import com.clean.architecture.demo.domain.model.RegisterEntity
import com.clean.architecture.demo.domain.model.StartPageState
import com.clean.architecture.demo.domain.model.VerifyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AppStartStateUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun getAppStartPageState(): Flow<StartPageState> {
        return accountRepository.getAccountStateEntity().map {
            when {
                it.shouldOpenBiometricAuth() -> StartPageState.SystemVerification
                it.shouldJump2LoginPage() -> StartPageState.Login
                else -> StartPageState.FirstIn
            }
        }
    }
}


class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend fun login(email: String, password: String): ApiResult<LoginResult> =
        authRepository.login(email, password)

    suspend fun logout(): ApiResult<String> {
        val result = authRepository.logout()
        if(result.isSuccess()) {
            tokenRepository.clearAllToken()
            settingsRepository.saveUseBiometricAuth(false)
        }
        return result
    }

    suspend fun register(registerEntity: RegisterEntity): ApiResult<String> {
        return authRepository.register(registerEntity)
    }

    suspend fun verifyCode(code: String): ApiResult<VerifyResult> {
        return authRepository.verify(code)
    }

    suspend fun useBiometricAuth() = settingsRepository.useBiometricAuth()

    suspend fun saveUseBiometricAuth(use: Boolean) = settingsRepository.saveUseBiometricAuth(use)
}

class AccountActionUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend fun deleteAccount(): ApiResult<String> {
        val result = accountRepository.deleteAccount()
        if(result.isSuccess()) {
            settingsRepository.clearLocalCache()
        }
        return result
    }

}


class TokenUseCase @Inject constructor(
    private val repository: TokenRepository
) {
    suspend fun saveVerifiedToken(token: String) {
        repository.saveVerifiedToken(token)
    }

    suspend fun saveUnverifiedToken(token: String) {
        repository.saveUnverifiedToken(token)
    }

    suspend fun clearAllToken() {
        repository.clearAllToken()
    }

}