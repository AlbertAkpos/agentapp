package com.youverify.agent_app_android.data.repository.verification.areas

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import javax.inject.Inject

class PrefAreasRemoteDataSourceImpl @Inject constructor(
    private val agentService: AgentService,
) : PrefAreasRemoteDataSource {

    override suspend fun saveAreas(prefAreaRequest: PrefAreaRequest): Result<*> {
        return try {
            val res = agentService.saveAreas(prefAreaRequest)

            when (res.isSuccessful) {
                true -> {
                    res.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error(Failure.ServerError)
                }
                false -> {
                    Result.Failed(res.errorBody())
                }
            }

        } catch (e: Throwable) {
            Result.Failed(e.message)
        }
    }
}