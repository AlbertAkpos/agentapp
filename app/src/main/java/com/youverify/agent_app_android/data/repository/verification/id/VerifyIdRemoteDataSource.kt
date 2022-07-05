package com.youverify.agent_app_android.data.repository.verification.id

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest

interface VerifyIdRemoteDataSource {
    suspend fun verifyId(verifyIDRequest: VerifyIDRequest): Result<*>
}