package com.clean.architecture.demo.domain.interfaces

import com.clean.architecture.demo.domain.model.AccountStateEntity
import com.clean.architecture.demo.domain.model.ApiResult
import com.clean.architecture.demo.domain.model.LoginResult
import com.clean.architecture.demo.domain.model.RegisterEntity
import com.clean.architecture.demo.domain.model.VerifyResult
import kotlinx.coroutines.flow.Flow


interface AccountRepository {
    suspend fun getAccountStateEntity(): Flow<AccountStateEntity>
    suspend fun deleteAccount(): ApiResult<String>
}

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<LoginResult>
    suspend fun logout(): ApiResult<String>
    suspend fun register(registerEntity: RegisterEntity): ApiResult<String>
    suspend fun verify(code: String): ApiResult<VerifyResult>
}

interface TokenRepository{
    suspend fun saveVerifiedToken(token: String)
    suspend fun saveUnverifiedToken(token: String)
    suspend fun getVerifiedToken(): String?
    suspend fun clearAllToken()
}

interface SettingsRepository {
    suspend fun clearLocalCache()
    suspend fun checkAgreement()
    suspend fun useBiometricAuth(): Flow<Boolean>
    suspend fun saveUseBiometricAuth(use: Boolean)
}
