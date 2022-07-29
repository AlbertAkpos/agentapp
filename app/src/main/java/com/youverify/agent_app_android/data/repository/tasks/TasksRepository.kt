package com.youverify.agent_app_android.data.repository.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.youverify.agent_app_android.data.mapper.entity
import com.youverify.agent_app_android.data.mapper.map
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.data.model.common.Domain
import com.youverify.agent_app_android.data.model.common.map
import com.youverify.agent_app_android.data.model.entity.TaskEntity
import com.youverify.agent_app_android.data.model.entity.domain
import com.youverify.agent_app_android.data.model.entity.entity
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadDomain
import com.youverify.agent_app_android.data.source.IAgentSource
import com.youverify.agent_app_android.data.source.ILocalSource
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import com.youverify.agent_app_android.util.AgentSharePreference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val source: IAgentSource,
    private val dispatcher: CoroutineDispatcher,
    private val localSource: ILocalSource,
    private val preference: AgentSharePreference
) : ITaskRepository {
    override suspend fun fetchAgentTasks(state: String?, status: String?): TasksDomain.AgentTasksResponse = withContext(dispatcher) {
        source.fetchAgentTasks(state, status).map()
    }

    override suspend fun startTask(taskId: String): TasksDomain.StartTaskResponse = withContext(dispatcher) {
        source.startTask(taskId).map()
    }


    override suspend fun getSubmissionMessages(verificationType: String): TasksDomain.MessagesResponse = withContext(dispatcher) {
       source.getSubmissionMessages(verificationType).map()
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

    override suspend fun addTask(taskItem:  TasksDomain.AgentTask, agentId: String) = withContext(dispatcher) {
        val items = taskItem.entity(null, agentId)
        localSource.addTask(items)
    }

    override suspend fun updateTask(taskItem:  TasksDomain.SubmitTask, taskDomain: TasksDomain.AgentTask, agentId: String) = withContext(dispatcher) {
        val entity = taskDomain.entity(taskItem, agentId)
        Timber.d("Undating task with id: ${entity.taskId} Entity\n  $entity")
        localSource.updateTask(entity)
    }

    override suspend fun deleteTask(taskItem:  TasksDomain.AgentTask, agentId: String) = withContext(dispatcher) {
        val items = taskItem.entity(null, agentId)
        localSource.deleteTask(items)
    }

    override suspend fun deleteTask(taskId: String) {
        Timber.d("Delete task after successful submission. Task id: $taskId")
        localSource.deleteTask(taskId)
    }

    override fun fetchOfflineTasks(agentId: String): LiveData<List<NotificationItem>> {
        return localSource.fetchOfflineTasks(agentId).map { it.map { item -> item.domain("dummy") } }
    }

    override fun fetchAgentId(): String {
        return preference.agentId
    }

    override suspend fun fetchAgentAnalyticsOnTasks(
        agentId: String,
        startDate: String,
        endDate: String
    ): Domain.AgentTasksAnalyticsResponse = withContext(dispatcher) {
         source.fetchAgentAnalyticsOnTasks(agentId, startDate, endDate).map()
    }
}