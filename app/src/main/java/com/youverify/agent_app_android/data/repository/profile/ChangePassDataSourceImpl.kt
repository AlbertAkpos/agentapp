package com.youverify.agent_app_android.data.repository.profile

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest
import com.youverify.agent_app_android.util.handleErrorMessage
import javax.inject.Inject

class ChangePassDataSourceImpl @Inject constructor(
    private val agentService : AgentService
    ): ChangePassDataSource {

    override suspend fun changePassword(changePassRequest: ChangePassRequest): Result<*> {
        return try{
            val res = agentService.changePassword(changePassRequest)

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