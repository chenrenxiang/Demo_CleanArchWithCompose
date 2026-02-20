package com.clean.architecture.demo.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// local key-value cache

private val Context.prefDataStore by preferencesDataStore(name = "mydemo")

class AppDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private fun get() = context.prefDataStore

    private val keyUnverifiedToken = stringPreferencesKey("unverified_token")
    private val keyToken = stringPreferencesKey("token")
    private val keyAgreement = booleanPreferencesKey("agreement")
    private val keyUseBiometricAuth = booleanPreferencesKey("use_biometric_auth")

    suspend fun saveUnverifiedToken(token: String) {
        get().edit { it[keyUnverifiedToken] = token }
    }

    fun getUnverifiedToken(): Flow<String?> = get().data.map { it[keyUnverifiedToken] }


    suspend fun saveToken(token: String) {
        get().edit { it[keyToken] = token }
    }

    fun getToken(): Flow<String?> = get().data.map { it[keyToken] }

    suspend fun clearAllTokens() {
        get().edit { it.remove(keyToken) }
        get().edit { it.remove(keyUnverifiedToken) }
    }

    suspend fun clearAll() {
        get().edit { it.clear() }
    }

    suspend fun setAgreementChecked(check: Boolean) {
        get().edit { it[keyAgreement] = check }
    }

    fun isAgreementChecked(): Flow<Boolean> = get().data.map { it[keyAgreement] ?: false  }

    suspend fun setUseBiometricAuth(use: Boolean) {
        get().edit { it[keyUseBiometricAuth] = use }
    }

    fun useBiometricAuth(): Flow<Boolean> = get().data.map { it[keyUseBiometricAuth] ?: false  }

}