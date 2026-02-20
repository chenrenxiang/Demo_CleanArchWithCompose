package com.clean.architecture.demo.data.api.mapper

import com.clean.architecture.demo.data.api.datamodel.ReqRegister
import com.clean.architecture.demo.data.api.datamodel.RespLogin
import com.clean.architecture.demo.data.api.datamodel.RespVerifyCode
import com.clean.architecture.demo.domain.model.LoginResult
import com.clean.architecture.demo.domain.model.RegisterEntity
import com.clean.architecture.demo.domain.model.VerifyResult


fun RespLogin.toLoginResult() = LoginResult(
    token = this.token,
    message = this.message,
    requiresTwoFactor = this.requiresTwoFactor
)

fun RespVerifyCode.toVerifyResult() = VerifyResult(
    token = this.token,
    message = this.message,
)

fun RegisterEntity.toReqRegister() = ReqRegister(
    email = this.email,
    name = this.name,
    kana = this.kana,
    password = this.password,
    passwordConfirmation = this.passwordTwice,
)