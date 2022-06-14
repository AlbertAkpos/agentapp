package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.data.model.tasks.TasksDomain

interface ITaskRepository {
    suspend fun fetchAgentTasks(): TasksDomain.AgentTasksResponse
}