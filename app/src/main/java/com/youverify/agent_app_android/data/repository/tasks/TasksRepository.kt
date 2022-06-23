package com.youverify.agent_app_android.data.repository.tasks

import com.youverify.agent_app_android.data.mapper.map
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadDomain
import com.youverify.agent_app_android.data.source.IAgentSource
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class TasksRepository @Inject constructor (private val source: IAgentSource, private val dispatcher: CoroutineDispatcher): ITaskRepository {
    override suspend fun fetchAgentTasks(): TasksDomain.AgentTasksResponse = withContext(dispatcher) {
        source.fetchAgentTasks().map()
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
}