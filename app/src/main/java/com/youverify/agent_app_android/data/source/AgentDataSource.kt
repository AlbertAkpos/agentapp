package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.api.UploadService
import com.youverify.agent_app_android.data.model.common.Domain
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class AgentDataSource @Inject constructor (private val service: AgentService, private val uploadService: UploadService): IAgentSource {
    override suspend fun fetchAgentTasks(state: String?, status: String?): TasksDto.AgentTasksResponse {
        return service.getAgentTasks(state, status)
    }

    override suspend fun startTask(taskId: String): TasksDto.StartTaskResponse {
        return service.startTask(taskId)
    }

    override suspend fun getRejectionMessages(): TasksDto.RejectionMessagesResponse {
        return service.getRejectionMessages()
    }

    override suspend fun getSubmissionMessages(verificationType: String): TasksDto.SubmissionMessagesResponse {
        return service.getSubmissionMessages(verificationType)
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

    override suspend fun submitFcmToken(request: Dto.FcmToken): Dto.GenericResponse {
        return service.submitFcmToken(request)
    }

    override suspend fun getTaskStatuses(): TasksDto.TaskStatusesResponse {
        return service.getTaskStatuses()
    }

    override suspend fun updateAgentStatus(request: Dto.UpdateAgentStatusRequest): Dto.UpdateAgentStatusResponse {
        return service.updateAgentStatus(request)
    }

    override suspend fun fetchAgentAnalyticsOnTasks(
        agentId: String,
        startDate: String,
        endDate: String
    ): Dto.AgentTasksAnalyticsResponse {
        return service.fetchAgentAnalyticsOnTasks(startDate, endDate)
    }
}