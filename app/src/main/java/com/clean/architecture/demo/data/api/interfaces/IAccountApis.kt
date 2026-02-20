package com.clean.architecture.demo.data.api.interfaces

import com.clean.architecture.demo.data.api.datamodel.ReqLogin
import com.clean.architecture.demo.data.api.datamodel.ReqRegister
import com.clean.architecture.demo.data.api.datamodel.ReqVerifyCode
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.POST

interface IAccountApis {


    @POST("api/register")
    suspend fun register(@Body register: ReqRegister): JsonElement

    @POST("api/login")
    suspend fun login(@Body login: ReqLogin): JsonElement

    @POST("api/verify-2fa")
    suspend fun verify2Fa(@Body verify: ReqVerifyCode): JsonElement

    @POST("api/logout")
    suspend fun logout(): JsonElement

    @POST("api/quit")
    suspend fun quit(): JsonElement


}