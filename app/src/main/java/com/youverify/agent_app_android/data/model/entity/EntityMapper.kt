package com.youverify.agent_app_android.data.model.entity

import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.mapper.entity
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto

fun TasksDomain.AgentTask.entity(submitTask: TasksDomain.SubmitTask?, agentId: String): TaskEntity.TaskItem {
    val candidate = TaskEntity.Candidate(
        lastName = candidate?.lastName,
        businessId = candidate?.businessId,
        createdAt = candidate?.createdAt,
        firstName = candidate?.firstName,
        id = candidate?.id,
        lastModifiedAt = candidate?.lastModifiedAt,
        mobile = candidate?.mobile,
        photo = candidate?.photo
    )

    val agentTask = TaskEntity.AgentTask(
        candidate = candidate,
        buildingNumber = buildingNumber,
        businessRegNumber = businessRegNumber,
        businessName = businessName,
        city = city,
        country = country,
        flatNumber = flatNumber,
        landmark = landmark,
        lastModifiedAt = lastModifiedAt,
        lga = lga,
        street = street,
        status = status,
        state = state,
        taskId = id,
        verificationType = verificationType
    )

    return TaskEntity.TaskItem(
        taskId = id,
        agentTask = agentTask,
        submitTask = submitTask?.entity(),
        agentId = agentId
    )
}

fun TaskEntity.TaskItem.domain(dummy: String): NotificationItem {
    val taskItem = this.domain()
    val domainSubmitTask = this.submitTask?.domain()
    return NotificationItem(
        image = R.drawable.ic_offline_task,
        accessText = "Offline task",
        addressText = taskItem.address,
        nameText = taskItem.candidate?.name ?: "",
        timeText = taskItem.time,
        taskItem = taskItem,
        submitTask = domainSubmitTask
    )
}

fun TaskEntity.TaskItem.domain(): TasksDomain.AgentTask {

    val candidate = TasksDomain.Candidate(
        lastName = agentTask.candidate?.lastName,
        businessId = agentTask.candidate?.businessId,
        createdAt = agentTask.candidate?.createdAt,
        firstName = agentTask.candidate?.firstName,
        id = agentTask.candidate?.id,
        lastModifiedAt = agentTask.candidate?.lastModifiedAt,
        mobile = agentTask.candidate?.mobile,
        photo = agentTask.candidate?.photo
    )


    return TasksDomain.AgentTask(
        candidate = candidate,
        buildingNumber = agentTask.buildingNumber,
        businessRegNumber = agentTask.businessRegNumber,
        businessName = agentTask.businessName,
        city = agentTask.city,
        country = agentTask.country,
        flatNumber = agentTask.flatNumber,
        landmark = agentTask.landmark,
        lastModifiedAt = agentTask.lastModifiedAt,
        lga = agentTask.lga,
        street = agentTask.street,
        status = agentTask.status,
        state = agentTask.state,
        id = taskId,
        verificationType = agentTask.verificationType
    )
}

fun TaskEntity.SubmitTask.domain(): TasksDomain.SubmitTask {
    return TasksDomain.SubmitTask(
        message = message,
        subitTaskRequest = subitTaskRequest.dto(),
        taskId = taskId,
        updateTaskRequest = updateTaskRequest.dto(),
        offlinePhotos = offlinePhotos,
        offlineSignature = offlineSignature
    )
}

fun TaskEntity.SubmitTaskRequest.dto(): TasksDto.SubmitTaskRequest {
    return TasksDto.SubmitTaskRequest(
        message = message
    )
}

fun TaskEntity.UpdateTaskRequest.dto(): TasksDto.UpdateTaskRequest {
    return TasksDto.UpdateTaskRequest(
        agentSignature = agentSignature,
        buildingType = buildingType,
        buildingColour = buildingColour,
        confirmedBy = confirmedBy,
        gatePresent = gatePresent,
        gateColor = gateColor,
        location = location.dto(),
        photos = photos.map { it.dto() }
    )
}

fun TaskEntity.Coordinates.dto(): TasksDto.Coordinates {
    return TasksDto.Coordinates(
        long, lat
    )
}

fun TaskEntity.UpdateTaskPhoto.dto(): TasksDto.UpdateTaskPhoto {
    return TasksDto.UpdateTaskPhoto(
        location = TasksDto.UpdateTaskLocation(long = location?.long, lat = location?.lat ),
        url = url
    )
}