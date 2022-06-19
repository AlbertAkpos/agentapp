package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import kotlinx.coroutines.flow.Flow

interface VerifyIdRepository {
    fun verifyId(verifyIDRequest: VerifyIDRequest) : Flow<Result<Any>>
}