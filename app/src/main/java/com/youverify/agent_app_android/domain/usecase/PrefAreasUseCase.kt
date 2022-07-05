package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.domain.repository.PrefAreasRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrefAreasUseCase @Inject constructor (
    private val prefAreasRepository: PrefAreasRepository)
    : FlowUseCase<PrefAreaRequest, Any> {

    override fun invoke(params: PrefAreaRequest?): Flow<Result<Any>> {
        return prefAreasRepository.saveAreas(params!!)
    }
}