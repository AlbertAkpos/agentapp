package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.data.model.common.Domain
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.data.model.login.LoginResponse

interface IAgentRepository {
    suspend fun updateAgentStatus(request: Dto.UpdateAgentStatusRequest): Domain.UpdateAgentResponse
}