package com.youverify.agent_app_android.data.repository.verification.refresh_token

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest
import com.youverify.agent_app_android.util.handleErrorMessage
import com.youverify.agent_app_android.core.functional.Result
import javax.inject.Inject

class TokenDataSourceImpl @Inject constructor(
    private val agentService: AgentService
) : TokenDataSource {
    override suspend fun refreshToken(tokenRequest: TokenRequest): Result<*> {
        return try {
            val res = agentService.refreshToken(tokenRequest)

            when (res.isSuccessful) {
                true -> {
                    res.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error(Failure.ServerError)
                }
                false -> {
                    val errorMessage = handleErrorMessage(res.errorBody()!!)
                    Result.Failed(errorMessage)
                }
            }
        } catch (e: Throwable) {
            Result.Failed(e.message)
        }
    }
}