package com.youverify.agent_app_android.core.functional

sealed class ResultState<T> {
    class Loading<T>(val message: String = ""): ResultState<T>()
    data class Success<T>(val data: T): ResultState<T>()
    data class Error<T>(val error: String): ResultState<T>()
}
