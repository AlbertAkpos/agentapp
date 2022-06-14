package com.youverify.agent_app_android.data.mapper

import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto

fun TasksDto.AgentTasksResponse.map(): TasksDomain.AgentTasksResponse {
    val listAgentTasks = arrayListOf<TasksDomain.AgentTask>()
    data?.docs?.let {
        for (doc in it) {
            val candidate = TasksDomain.Candidate(
                lastName = doc.candidate?.lastName,
                firstName = doc.candidate?.firstName,
                businessId = doc.candidate?.businessId,
                createdAt = doc.candidate?.createdAt,
                id = doc.candidate?.id,
                lastModifiedAt = doc.candidate?.lastModifiedAt,
                mobile = doc.candidate?.mobile,
                photo = doc.candidate?.photo
            )
            val agentTasks = TasksDomain.AgentTask(
                flatNumber = doc.address?.flatNumber ?: "",
                buildingNumber = doc.address?.buildingNumber?.toString() ?: "",
                businessName = doc.business?.name ?: "",
                businessRegNumber = doc.business?.registrationNumber ?: "",
                city = doc.address?.city ?: "",
                state = doc.address?.state ?: "",
                country = doc.address?.country ?: "",
                landmark = doc.address?.landmark ?: "",
                lga = doc.address?.lga ?: "",
                status = doc.status,
                street = doc.address?.street ?: "",
                verificationType = doc.verificationType ?: "",
                candidate = candidate
            )
            listAgentTasks.add(agentTasks)
        }
    }

    return TasksDomain.AgentTasksResponse(
        success = success == true,
        message = message,
        taskItems = listAgentTasks
    )
}