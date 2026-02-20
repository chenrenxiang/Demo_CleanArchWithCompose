package com.clean.architecture.demo.common


sealed class State<out T> {
    object Idle : State<Nothing>()  //initial state
    data class Success<out T>(val value: T) : State<T>()
    data class Failure(
        val message: String? = null,
        val id: Long = System.currentTimeMillis()
    ) : State<Nothing>()
    object Loading: State<Nothing>()

    fun failureMsg() = if (this is Failure) message else null

    fun data() = if (this is Success) value else null
    fun isLoading() = this is Loading
    fun isSuccess() = this is Success

}





