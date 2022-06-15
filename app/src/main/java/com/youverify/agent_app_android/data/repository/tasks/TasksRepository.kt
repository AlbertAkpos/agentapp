package com.youverify.agent_app_android.data.repository.tasks

import com.youverify.agent_app_android.data.mapper.map
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.source.IAgentSource
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
}