package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.data.model.common.Domain
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.data.model.common.map
import com.youverify.agent_app_android.data.source.IAgentSource
import javax.inject.Inject

class AgentRepository @Inject constructor(private val source: IAgentSource) : IAgentRepository {
    override suspend fun updateAgentStatus(request: Dto.UpdateAgentStatusRequest): Domain.UpdateAgentResponse {
        return source.updateAgentStatus(request).map()
    }
}