package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.model.tasks.TasksDto

interface IAgentSource {
    suspend fun fetchAgentTasks(): TasksDto.AgentTasksResponse
    suspend fun startTask(taskId: String) : TasksDto.StartTaskResponse
    suspend fun getRejectionMessages(): TasksDto.RejectionMessagesResponse
    suspend fun getSubmissionMessages(): TasksDto.SubmissionMessagesResponse
    suspend fun submitTaskRejection(rejectRequest: TasksDto.RejectTaskAnswers, taskId: String): TasksDto.GenericResponse
    suspend fun updateTaskRequest( taskId: String, request: TasksDto.UpdateTaskRequest): TasksDto.GenericResponse
}