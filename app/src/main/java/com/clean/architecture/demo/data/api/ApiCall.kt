package com.clean.architecture.demo.data.api

import com.clean.architecture.demo.BuildConfig
import com.clean.architecture.demo.data.api.datamodel.RspApiFailure
import com.clean.architecture.demo.domain.model.ApiResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.decodeFromJsonElement
import retrofit2.HttpException
import kotlin.reflect.typeOf

val defaultJson by lazy {
    Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
}

suspend inline fun <reified T> apiCall(apiCall: suspend () -> JsonElement?): ApiResult<T> {
    return try {
        val element = apiCall()
        if(typeOf<T>() == typeOf<Unit>()) {
            return ApiResult.Success(Unit as T)
        } else if (element == null || element is JsonNull) {
            return ApiResult.Exception(NullDataException())
        }
        ApiResult.Success(defaultJson.decodeFromJsonElement<T>(element))
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        if(errorBody.isNullOrBlank()) {
            val apiException = ApiException(e.code(), e.message())
            printApiException(apiException)
            ApiResult.Exception(apiException)
        } else {
            val failure = defaultJson.decodeFromString<RspApiFailure>(errorBody)
            ApiResult.Failure(failure.message)
        }
    } catch (e: Exception) {
        printApiException(e)
        ApiResult.Exception(e)
    }
}

class ApiException(val code: Int? = null, override val message: String) : Exception(message)


class NullDataException(
    override val message: String = "Request succeeded, but response data is null."
) : Exception(message)



fun printApiException(e: Exception) {
    if(BuildConfig.DEBUG) {
        e.printStackTrace()
    }
}