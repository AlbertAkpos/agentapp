package com.youverify.agent_app_android.data.repository.resetpassword.datasourceImpl

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.data.repository.resetpassword.datasource.ResetPassRemoteDataSource
import com.youverify.agent_app_android.util.handleErrorMessage
import javax.inject.Inject

class ResetPassDataSourceImpl @Inject constructor(
    private val agentService: AgentService
    ): ResetPassRemoteDataSource {

    override suspend fun sendResetEmail(email: Email): Result<*> {
        return try {
            val res = agentService.sendResetEmail(email)

            when(res.isSuccessful){
                true -> {
                    res.body()?.let {
                        Result.Success(it)
                    }?: Result.Error(Failure.ServerError)
                }
                false -> {
                    val errorMessage = handleErrorMessage(res.errorBody()!!)
                    Result.Failed(errorMessage)
                }
            }
        }catch (e: Throwable){
            Result.Failed(e.message)
        }
    }
}