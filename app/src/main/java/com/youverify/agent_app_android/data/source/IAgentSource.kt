package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.model.tasks.TasksDto
import retrofit2.http.GET

interface IAgentSource {
    suspend fun fetchAgentTasks(): TasksDto.AgentTasksResponse
    suspend fun startTask(taskId: String) : TasksDto.StartTaskResponse
    suspend fun getRejectionMessages(): TasksDto.RejectionMessagesResponse
    suspend fun getSubmissionMessages(): TasksDto.SubmissionMessagesResponse
}