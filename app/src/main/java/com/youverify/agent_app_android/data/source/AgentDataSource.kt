package com.youverify.agent_app_android.data.source

import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import javax.inject.Inject

class AgentDataSource @Inject constructor (private val service: AgentService): IAgentSource {
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

    override suspend fun submitTaskRejection(rejectRequest: TasksDto.RejectTaskAnswers, taskId: String): TasksDto.GenericResponse {
        return service.submitTaskRejection(rejectRequest, taskId)
    }

    override suspend fun updateTaskRequest(taskId: String, request: TasksDto.UpdateTaskRequest): TasksDto.GenericResponse {
        return service.updateTask(taskId, request)
    }
}