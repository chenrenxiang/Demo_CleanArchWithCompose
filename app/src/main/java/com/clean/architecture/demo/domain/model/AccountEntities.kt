package com.clean.architecture.demo.domain.model

import kotlinx.serialization.Serializable



data class AccountStateEntity(
    val token: String? = null,
    val unverifiedToken: String? = null,
    val agreementChecked: Boolean = false,
    val useBiometricAuth: Boolean = false
) {
    fun shouldOpenBiometricAuth(): Boolean {
        return token?.isNotBlank() == true && useBiometricAuth
    }

    fun shouldJump2LoginPage(): Boolean {
        //account verified, but biometric auth is not checked
        if(token?.isNotBlank() == true && !useBiometricAuth) return true
        //account registered, but not verified yet
        if(token.isNullOrBlank() && unverifiedToken?.isNotBlank() == true) return true
        //has verified municipal code & checked agreement
        if(agreementChecked) return true

        return false
    }
}

data class LoginResult(
    val token: String,
    val message: String? = null,
    val requiresTwoFactor: Boolean? = null,
)

data class VerifyResult(
    val token: String,
    val message: String? = null,
    val user: UserEntity? = null,
)

data class RegisterEntity(
    val email: String,
    val name: String,
    val kana: String,
    val password: String,
    val passwordTwice: String,
)

@Serializable
data class UserEntity(
    val id: Int,
    val name: String,
    val kana: String,
    val email: String,
)

sealed class StartPageState {
    object FirstIn : StartPageState()
    object Agreement : StartPageState()
    object Login : StartPageState()
    object Verify : StartPageState()
    object SystemVerification : StartPageState()
}