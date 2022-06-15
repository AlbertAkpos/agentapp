package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.core.functional.base.TokenParamUseCase
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.domain.repository.PrefAreasRepository
import com.youverify.agent_app_android.domain.repository.VerifyIdRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyIdUseCase @Inject constructor (
    private val verifyIdRepository: VerifyIdRepository)
    : TokenParamUseCase<VerifyIDRequest, Any> {

    override fun invoke(params: VerifyIDRequest?, token: String): Flow<Result<Any>> {
        return verifyIdRepository.verifyId(params!!, token)
    }
}