package com.youverify.agent_app_android.data.repository.login

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.data.model.login.LoginRequest
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
                    Result.Failed(res.errorBody())
                }
            }
        }catch (e: Throwable){
            Result.Failed(e.message)
        }
    }

    override suspend fun submitFcmToken(request: Dto.FcmToken): Dto.GenericResponse {
        return agentService.submitFcmToken(request)
    }
}