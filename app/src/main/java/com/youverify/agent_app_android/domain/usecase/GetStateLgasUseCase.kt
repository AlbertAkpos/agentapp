package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.core.functional.base.ListUseCase
import com.youverify.agent_app_android.domain.repository.StateLgaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStateLgasUseCase @Inject constructor(
    private val stateLgaRepository: StateLgaRepository
    ): ListUseCase<String, String> {

    override suspend fun invoke(params: String?): List<String>? = stateLgaRepository.getStateLgas(params!!)
}