package com.youverify.agent_app_android.data.mapper

import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.data.model.entity.TaskEntity
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

            val verificationType = when {
                doc.verificationType?.equals("INDIVIDUAL", true) == true -> "Live photo"
                doc.verificationType?.equals(
                    "GUARANTOR",
                    true
                ) == true -> "Guarantor address verification"
                doc.verificationType?.equals(
                    "BUSINESS",
                    true
                ) == true -> "Business address verification"
                else -> "Live photo"
            }

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
                verificationType = verificationType ?: "",
                candidate = candidate,
                id = doc.id ?: "",
                lastModifiedAt = doc.lastModifiedAt ?: ""
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

fun TasksDto.StartTaskResponse.map(): TasksDomain.StartTaskResponse {
    return TasksDomain.StartTaskResponse(
        success = this.success == true,
        message = this.message ?: ""
    )
}

fun TasksDto.RejectionMessagesResponse.map(): TasksDomain.MessagesResponse {
    return TasksDomain.MessagesResponse(
        success = this.success == true,
        data = data
    )
}

fun TasksDto.SubmissionMessagesResponse.map(): TasksDomain.MessagesResponse {
    return TasksDomain.MessagesResponse(
        success = this.success == true,
        data = data
    )
}

fun TasksDto.GenericResponse.map(): TasksDomain.GenericResponse {
    return TasksDomain.GenericResponse(
        success = success == true,
        message = message,
        statusCode = statusCode
    )
}

fun TasksDto.TaskStatusesResponse.map(): TasksDomain.TasksStatusesResponse {
    return TasksDomain.TasksStatusesResponse(
        sucess = success == true,
        message, data
    )
}

fun TasksDomain.AgentTask.notification(): NotificationItem {
    return NotificationItem(
        image = R.drawable.ic_offline_task,
        accessText = "Offline task",
        addressText = address,
        nameText = candidate?.name ?: "",
        timeText = time,
        taskItem = this
    )
}

fun TasksDto.SubmitTaskRequest.entity(): TaskEntity.SubmitTaskRequest {
    return TaskEntity.SubmitTaskRequest(
        message = message
    )
}

fun TasksDomain.SubmitTask.entity(): TaskEntity.SubmitTask {
    return TaskEntity.SubmitTask(
        updateTaskRequest = updateTaskRequest.entity(),
        taskId = taskId,
        message = message,
        subitTaskRequest = subitTaskRequest.entity(),
        offlinePhotos = offlinePhotos,
        offlineSignature = offlineSignature
    )
}




fun TasksDto.UpdateTaskRequest.entity(): TaskEntity.UpdateTaskRequest {
    return TaskEntity.UpdateTaskRequest(
        agentSignature = agentSignature,
        buildingColour = buildingColour,
        buildingType = buildingType,
        confirmedBy = confirmedBy,
        gateColor = gateColor,
        gatePresent = gatePresent,
        location = location.entity(),
        photos = photos.map { it.entity() }
    )
}

fun TasksDto.Coordinates.entity(): TaskEntity.Coordinates {
    return TaskEntity.Coordinates(
        long, lat
    )
}

fun TasksDto.UpdateTaskPhoto.entity(): TaskEntity.UpdateTaskPhoto {
    return TaskEntity.UpdateTaskPhoto(
        location = TaskEntity.Coordinates(location?.long, location?.lat),
        url = url
    )
}


fun TasksDomain.Candidate.entity(): TaskEntity.Candidate {
    return TaskEntity.Candidate(
        lastName = lastName,
        firstName = firstName,
        businessId = businessId,
        createdAt = createdAt,
        id = id,
        lastModifiedAt = lastModifiedAt,
        mobile = mobile,
        photo = photo
    )
}


