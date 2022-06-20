package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.api.UploadService
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class AgentDataSource @Inject constructor (private val service: AgentService, private val uploadService: UploadService): IAgentSource {
    override suspend fun fetchAgentTasks(): TasksDto.AgentTasksResponse {
        return service.getAgentTasks()
    }

    override suspend fun startTask(taskId: String): TasksDto.StartTaskResponse {
        return service.startTask(taskId)
    }

    override suspend fun getRejectionMessages(): TasksDto.RejectionMessagesResponse {
        return service.getRejectionMessages()
    }

    override suspend fun getSubmissionMessages(): TasksDto.SubmissionMessagesResponse {
        return service.getSubmissionMessages()
    }

    override suspend fun submitTask(submitRequest: TasksDto.SubmitTaskRequest, taskId: String): TasksDto.GenericResponse {
        return service.submitTask(submitRequest, taskId)
    }

    override suspend fun updateTaskRequest(taskId: String, request: TasksDto.UpdateTaskRequest): TasksDto.GenericResponse {
        return service.updateTask(taskId, request)
    }

    override suspend fun doImageUpload(file: List<MultipartBody.Part>): UploadImageResponse {
        return uploadService.uploadImage(file)
    }
}