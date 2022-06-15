package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.data.model.tasks.TasksDomain

interface ITaskRepository {
    suspend fun fetchAgentTasks(): TasksDomain.AgentTasksResponse
    suspend fun startTask(taskId: String) : TasksDomain.StartTaskResponse
    suspend fun getRejectionMessages(): TasksDomain.MessagesResponse
    suspend fun getSubmissionMessages(): TasksDomain.MessagesResponse
}