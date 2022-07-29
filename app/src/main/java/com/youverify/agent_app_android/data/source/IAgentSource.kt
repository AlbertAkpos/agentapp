package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.model.common.Domain
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.data.model.entity.TaskEntity
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Path
import retrofit2.http.Query

interface IAgentSource {
    suspend fun fetchAgentTasks(state: String?, status: String?): TasksDto.AgentTasksResponse
    suspend fun startTask(taskId: String) : TasksDto.StartTaskResponse
    suspend fun getRejectionMessages(): TasksDto.RejectionMessagesResponse
    suspend fun getSubmissionMessages(verificationType: String): TasksDto.SubmissionMessagesResponse
    suspend fun submitTask(submitRequest: TasksDto.SubmitTaskRequest, taskId: String): TasksDto.GenericResponse
    suspend fun updateTaskRequest( taskId: String, request: TasksDto.UpdateTaskRequest): TasksDto.GenericResponse
    suspend fun doImageUpload(file: List<MultipartBody.Part>): UploadImageResponse
    suspend fun submitFcmToken(request: Dto.FcmToken): Dto.GenericResponse
    suspend fun getTaskStatuses() : TasksDto.TaskStatusesResponse
    suspend fun updateAgentStatus(request: Dto.UpdateAgentStatusRequest): Dto.UpdateAgentStatusResponse
    suspend fun fetchAgentAnalyticsOnTasks(agentId: String, startDate: String, endDate: String): Dto.AgentTasksAnalyticsResponse

}