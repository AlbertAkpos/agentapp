package com.youverify.agent_app_android.domain.repository

import androidx.lifecycle.LiveData
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.data.model.common.Domain
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.data.model.entity.TaskEntity
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadDomain
import okhttp3.MultipartBody

interface ITaskRepository {
    suspend fun fetchAgentTasks(state: String?, status: String?): TasksDomain.AgentTasksResponse
    suspend fun startTask(taskId: String) : TasksDomain.StartTaskResponse
    suspend fun getSubmissionMessages(verificationType: String): TasksDomain.MessagesResponse
    suspend fun submitTask(request: TasksDto.SubmitTaskRequest, taskId: String): TasksDomain.GenericResponse
    suspend fun updateTask(taskId: String, request: TasksDto.UpdateTaskRequest): TasksDomain.GenericResponse
    suspend fun uploadImages(files: List<MultipartBody.Part>): UploadDomain.UploadResponse
    suspend fun getTaskStatuses() : TasksDomain.TasksStatusesResponse
    suspend fun addTask(taskItem: TasksDomain.AgentTask, agentId: String)
    suspend fun updateTask(taskItem:  TasksDomain.SubmitTask, taskDomain: TasksDomain.AgentTask, agentId: String)
    suspend fun deleteTask(taskItem:  TasksDomain.AgentTask, agentId: String)
    suspend fun deleteTask(taskId: String)
    fun fetchOfflineNotifications(agentId: String): LiveData<List<NotificationItem>>
    suspend fun getOfflineNotifications(agentId: String): List<NotificationItem>
    fun fetchAgentId(): String
    suspend fun fetchAgentAnalyticsOnTasks(agentId: String, startDate: String, endDate: String): Domain.AgentTasksAnalyticsResponse
    fun fetchOfflineTasks(agentId: String): LiveData<List<TasksDomain.AgentTask>>


}