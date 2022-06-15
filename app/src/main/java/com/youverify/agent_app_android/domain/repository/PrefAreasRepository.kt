package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import kotlinx.coroutines.flow.Flow

interface PrefAreasRepository {
    fun saveAreas(prefAreaRequest: PrefAreaRequest, token: String) : Flow<Result<Any>>
}