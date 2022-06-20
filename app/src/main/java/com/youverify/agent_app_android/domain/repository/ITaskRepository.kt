package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadDomain
import okhttp3.MultipartBody

interface ITaskRepository {
    suspend fun fetchAgentTasks(): TasksDomain.AgentTasksResponse
    suspend fun startTask(taskId: String) : TasksDomain.StartTaskResponse
    suspend fun getRejectionMessages(): TasksDomain.MessagesResponse
    suspend fun getSubmissionMessages(): TasksDomain.MessagesResponse
    suspend fun submitTask(request: TasksDto.SubmitTaskRequest, taskId: String): TasksDomain.GenericResponse
    suspend fun updateTask(taskId: String, request: TasksDto.UpdateTaskRequest): TasksDomain.GenericResponse
    suspend fun uploadImages(files: List<MultipartBody.Part>): UploadDomain.UploadResponse
}