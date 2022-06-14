package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.model.tasks.TasksDto

interface IAgentSource {
    suspend fun fetchAgentTasks(): TasksDto.AgentTasksResponse
}