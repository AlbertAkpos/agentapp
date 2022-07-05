package com.youverify.agent_app_android.data.repository.verification.id

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.util.handleErrorMessage
import javax.inject.Inject

class VerifyIdRemoteDataSourceImpl @Inject constructor(
    private val agentService: AgentService,
) : VerifyIdRemoteDataSource {

    override suspend fun verifyId(verifyIDRequest: VerifyIDRequest): Result<*> {
        return try {
            val res = agentService.verifyId(verifyIDRequest)

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