package com.clean.architecture.demo.domain.model

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val msg: String?) : ApiResult<Nothing>()
    data class Exception(val e: kotlin.Exception) : ApiResult<Nothing>()

    fun isSuccess() = this is Success

    fun response(): T? = if (this is Success) data else null

    fun errorMsg(): String? = when (this) {
        is Failure -> msg
        is Exception -> e.message
        else -> null
    }

    /**
     * make sure the result is success before call this method,
     * or it will throw exception
     */
    fun successData(): T = (this as Success).data

}
