package com.youverify.agent_app_android.data.repository.login

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.util.handleErrorMessage
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val agentService : AgentService
    ): LoginRemoteDataSource {

    override suspend fun login(loginRequest: LoginRequest): Result<*> {
        return try{
            val res = agentService.loginAgent(loginRequest)

            when(res.isSuccessful){
                true -> {
                    res.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error(Failure.ServerError)
                }
                false ->{
                    val errorMessage = handleErrorMessage(res.errorBody()!!)
                    Result.Failed(errorMessage)
                }
            }
        }catch (e: Throwable){
            Result.Failed(e.message)
        }
    }
}