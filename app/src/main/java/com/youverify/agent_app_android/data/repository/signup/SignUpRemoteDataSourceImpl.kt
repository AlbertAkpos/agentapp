package com.youverify.agent_app_android.data.repository.signup

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.util.handleErrorMessage
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
                    val errorMessage = handleErrorMessage(res.errorBody()!!)
                    Result.Failed(errorMessage)
                }
            }

        }catch (e: Throwable){
            Result.Failed(e.message)
        }
    }

}