package com.youverify.agent_app_android.data.repository.signup.datasourceImpl

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.repository.signup.datasource.SignUpRemoteDataSource
import javax.inject.Inject

class SignUpRemoteDataSourceImpl @Inject constructor(
    private val agentService: AgentService,
) : SignUpRemoteDataSource {


    override suspend fun signUp(signUpRequest: SignUpRequest): Result<*> {
        return try{
            val res = agentService.signUpAgent(signUpRequest)

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

}