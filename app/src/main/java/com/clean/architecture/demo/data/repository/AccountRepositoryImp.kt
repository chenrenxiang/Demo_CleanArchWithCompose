package com.clean.architecture.demo.data.repository

import com.clean.architecture.demo.data.api.apiCall
import com.clean.architecture.demo.data.api.datamodel.ReqLogin
import com.clean.architecture.demo.data.api.datamodel.ReqVerifyCode
import com.clean.architecture.demo.data.api.datamodel.RespLogin
import com.clean.architecture.demo.data.api.datamodel.RespMessage
import com.clean.architecture.demo.data.api.datamodel.RespRegister
import com.clean.architecture.demo.data.api.datamodel.RespVerifyCode
import com.clean.architecture.demo.data.api.interfaces.IAccountApis
import com.clean.architecture.demo.data.api.mapper.toLoginResult
import com.clean.architecture.demo.data.api.mapper.toReqRegister
import com.clean.architecture.demo.data.api.mapper.toVerifyResult
import com.clean.architecture.demo.data.datastore.AppDataStore
import com.clean.architecture.demo.domain.repository.AccountRepository
import com.clean.architecture.demo.domain.repository.AuthRepository
import com.clean.architecture.demo.domain.repository.SettingsRepository
import com.clean.architecture.demo.domain.repository.TokenRepository
import com.clean.architecture.demo.domain.model.AccountStateEntity
import com.clean.architecture.demo.domain.model.ApiResult
import com.clean.architecture.demo.domain.model.LoginResult
import com.clean.architecture.demo.domain.model.RegisterEntity
import com.clean.architecture.demo.domain.model.VerifyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenRepositoryImp @Inject constructor(
    val dataStore: AppDataStore
): TokenRepository {
    override suspend fun saveVerifiedToken(token: String) {
        dataStore.saveToken(token)
    }

    override suspend fun saveUnverifiedToken(token: String) {
        dataStore.saveUnverifiedToken(token)
    }

    override suspend fun getVerifiedToken(): String? {
        return dataStore.getToken().firstOrNull()
    }

    override suspend fun clearAllToken() {
        dataStore.clearAllTokens()
    }

}

@Singleton
class AccountRepositoryImp @Inject constructor(
    val dataStore: AppDataStore,
    val api: IAccountApis
) : AccountRepository {

    override suspend fun getAccountStateEntity(): Flow<AccountStateEntity> {
        return combine(
            dataStore.getUnverifiedToken(),
            dataStore.getToken(),
            dataStore.isAgreementChecked(),
            dataStore.useBiometricAuth()
        ) { token, unverifiedToken, checked, useBiometricAuth ->
            AccountStateEntity(
                token = token,
                unverifiedToken = unverifiedToken,
                agreementChecked = checked,
                useBiometricAuth = useBiometricAuth,
            )
        }
    }

    override suspend fun deleteAccount(): ApiResult<String> {
        val resp = apiCall<RespMessage> { api.quit() }
        return if(resp.isSuccess()) {
            ApiResult.Success(resp.successData().message)
        } else {
            ApiResult.Failure(resp.errorMsg())
        }
    }

}

@Singleton
class SettingsRepositoryImp @Inject constructor(
    private val dataStore: AppDataStore,
) : SettingsRepository {
    override suspend fun clearLocalCache() {
        dataStore.clearAll()
    }

    override suspend fun checkAgreement() {
        dataStore.setAgreementChecked(true)
    }

    override suspend fun useBiometricAuth(): Flow<Boolean> {
        return dataStore.useBiometricAuth()
    }

    override suspend fun saveUseBiometricAuth(use: Boolean) {
        dataStore.setUseBiometricAuth(use)
    }

}

@Singleton
class AuthRepositoryImp @Inject constructor(
    val api: IAccountApis,
): AuthRepository {

    override suspend fun login(email: String, password: String): ApiResult<LoginResult> {
        val result = apiCall<RespLogin> {
            api.login(ReqLogin(email, password))
        }
        return if(result.isSuccess()) {
            ApiResult.Success(result.successData().toLoginResult())
        } else {
            ApiResult.Failure(result.errorMsg())
        }
    }

    override suspend fun logout(): ApiResult<String> {
        val resp = apiCall<RespMessage> { api.logout() }
        return if(resp.isSuccess()) {
            ApiResult.Success(resp.successData().message)
        } else {
            ApiResult.Failure(resp.errorMsg())
        }
    }

    override suspend fun register(registerEntity: RegisterEntity): ApiResult<String> {
        val resp = apiCall<RespRegister> { api.register(registerEntity.toReqRegister()) }
        return if(resp.isSuccess()) {
            ApiResult.Success(resp.successData().token)
        } else {
            ApiResult.Failure(resp.errorMsg())
        }
    }

    override suspend fun verify(code: String, ): ApiResult<VerifyResult> {
        val resp = apiCall<RespVerifyCode> { api.verify2Fa(ReqVerifyCode(code)) }
        return if(resp.isSuccess()) {
            ApiResult.Success(resp.successData().toVerifyResult())
        } else {
            ApiResult.Failure(resp.errorMsg())
        }
    }



}