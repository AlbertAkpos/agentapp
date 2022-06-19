package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.domain.repository.VerifyIdRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyIdUseCase @Inject constructor (
    private val verifyIdRepository: VerifyIdRepository)
    : FlowUseCase<VerifyIDRequest, Any> {

    override fun invoke(params: VerifyIDRequest?): Flow<Result<Any>> {
        return verifyIdRepository.verifyId(params!!)
    }
}