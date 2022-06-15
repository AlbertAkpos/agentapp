package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.core.functional.base.TokenParamUseCase
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.domain.repository.PrefAreasRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrefAreasUseCase @Inject constructor (
    private val prefAreasRepository: PrefAreasRepository)
    : TokenParamUseCase<PrefAreaRequest, Any> {

    override fun invoke(params: PrefAreaRequest?, token: String): Flow<Result<Any>> {
        return prefAreasRepository.saveAreas(params!!, token)
    }
}