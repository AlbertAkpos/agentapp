package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import javax.inject.Inject

class AgentDataSource @Inject constructor (private val service: AgentService): IAgentSource {
    override suspend fun fetchAgentTasks(): TasksDto.AgentTasksResponse {
        return service.getAgentTasks()
    }
}