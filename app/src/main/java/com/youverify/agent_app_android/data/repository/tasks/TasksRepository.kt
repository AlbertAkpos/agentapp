package com.youverify.agent_app_android.data.repository.tasks

import com.youverify.agent_app_android.data.mapper.map
import com.youverify.agent_app_android.data.model.entity.TaskEntity
import com.youverify.agent_app_android.data.model.entity.toEntity
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadDomain
import com.youverify.agent_app_android.data.source.IAgentSource
import com.youverify.agent_app_android.data.source.ILocalSource
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class TasksRepository @Inject constructor (private val source: IAgentSource, private val dispatcher: CoroutineDispatcher, private val localSource: ILocalSource): ITaskRepository {
    override suspend fun fetchAgentTasks(state: String?, status: String?): TasksDomain.AgentTasksResponse = withContext(dispatcher) {
        source.fetchAgentTasks(state, status).map()
    }

    override suspend fun startTask(taskId: String): TasksDomain.StartTaskResponse = withContext(dispatcher) {
        source.startTask(taskId).map()
    }

    override suspend fun getRejectionMessages(): TasksDomain.MessagesResponse = withContext(dispatcher) {
        source.getRejectionMessages().map()
    }

    override suspend fun getSubmissionMessages(): TasksDomain.MessagesResponse = withContext(dispatcher) {
       source.getSubmissionMessages().map()
    }

    override suspend fun submitTask(request: TasksDto.SubmitTaskRequest, taskId: String): TasksDomain.GenericResponse = withContext(dispatcher) {
        source.submitTask(request, taskId).map()
    }

    override suspend fun updateTask(taskId: String, request: TasksDto.UpdateTaskRequest): TasksDomain.GenericResponse = withContext(dispatcher) {
        source.updateTaskRequest(taskId, request).map()
    }

    override suspend fun uploadImages(files: List<MultipartBody.Part>): UploadDomain.UploadResponse = withContext(dispatcher) {
        source.doImageUpload(files).map()
    }

    override suspend fun getTaskStatuses(): TasksDomain.TasksStatusesResponse = withContext(dispatcher) {
        source.getTaskStatuses().map()
    }

    override suspend fun addTask(vararg taskItem:  TasksDomain.AgentTask) = withContext(dispatcher) {
        val items = taskItem.map { it.toEntity() }
        localSource.addTask(*items.toTypedArray())
    }

    override suspend fun updateTask(vararg taskItem:  TasksDomain.AgentTask) = withContext(dispatcher) {
        val items = taskItem.map { it.toEntity() }
        localSource.updateTask(*items.toTypedArray())
    }

    override suspend fun deleteTask(vararg taskItem:  TasksDomain.AgentTask) = withContext(dispatcher) {
        val items = taskItem.map { it.toEntity() }
        localSource.deleteTask(*items.toTypedArray())
    }
}