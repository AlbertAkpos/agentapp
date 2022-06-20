package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import okhttp3.MultipartBody

interface IAgentSource {
    suspend fun fetchAgentTasks(): TasksDto.AgentTasksResponse
    suspend fun startTask(taskId: String) : TasksDto.StartTaskResponse
    suspend fun getRejectionMessages(): TasksDto.RejectionMessagesResponse
    suspend fun getSubmissionMessages(): TasksDto.SubmissionMessagesResponse
    suspend fun submitTask(submitRequest: TasksDto.SubmitTaskRequest, taskId: String): TasksDto.GenericResponse
    suspend fun updateTaskRequest( taskId: String, request: TasksDto.UpdateTaskRequest): TasksDto.GenericResponse
    suspend fun doImageUpload(file: List<MultipartBody.Part>): UploadImageResponse
}