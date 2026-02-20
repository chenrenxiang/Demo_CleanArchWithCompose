package com.clean.architecture.demo.data.api.datamodel

import kotlinx.serialization.Serializable


@Serializable
data class ReqLogin(
    val email: String,
    val password: String,
)
@Serializable
data class RespLogin(
    val message: String,
    val token: String,
    val requiresTwoFactor: Boolean? = null,
)

@Serializable
data class ReqRegister(
    val email: String,
    val name: String,
    val kana: String,
    val password: String,
    val passwordConfirmation: String,
)

@Serializable
data class RespRegister(val message: String, val token: String)

@Serializable
data class ReqVerifyCode(val code: String)

@Serializable
data class RespVerifyCode(val message: String, val token: String)

@Serializable
data class RespMessage(val message: String)


