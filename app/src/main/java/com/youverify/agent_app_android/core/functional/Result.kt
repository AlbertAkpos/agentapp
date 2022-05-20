package com.youverify.agent_app_android.core.functional

import com.youverify.agent_app_android.data.model.signup.SignUpResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()        // Status success and data of the result
    data class Error(val failure: Failure) : Result<Nothing>() // Status Error an error message
    data class Failed<F>(val errorMessage:F) : Result<F>(),
        Flow<Result<SignUpResponseData?>> {     // Status Failed message
        override suspend fun collect(collector: FlowCollector<Result<SignUpResponseData?>>) {
            TODO("Not yet implemented")
        }
    }


    // Status error and error message
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failed<*>-> "Success[data=$errorMessage]"
            is Error -> "Error[exception=$failure]"
        }
    }
}