package com.youverify.agent_app_android.data.repository.verification.areas

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest

interface PrefAreasRemoteDataSource {
    suspend fun saveAreas(prefAreaRequest: PrefAreaRequest, token: String): Result<*>
}