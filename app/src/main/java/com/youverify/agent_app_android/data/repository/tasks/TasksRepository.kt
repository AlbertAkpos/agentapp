package com.youverify.agent_app_android.data.repository.tasks

import com.youverify.agent_app_android.data.mapper.map
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.source.IAgentSource
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import javax.inject.Inject

class TasksRepository @Inject constructor (private val source: IAgentSource): ITaskRepository {
    override suspend fun fetchAgentTasks(): TasksDomain.AgentTasksResponse {
        return source.fetchAgentTasks().map()
    }
}