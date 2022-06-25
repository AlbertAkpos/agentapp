package com.youverify.agent_app_android.data.model.entity

import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.data.model.tasks.TasksDomain

fun TasksDomain.AgentTask.entity(): TaskEntity.TaskItem {
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

    return TaskEntity.TaskItem(
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
}

fun TaskEntity.TaskItem.domain(dummy: String): NotificationItem {
    val taskItem = this.domain()
    return NotificationItem(
        image = R.drawable.ic_offline_task,
        accessText = "Offline task",
        addressText = address,
        nameText = taskItem.candidate?.name ?: "",
        timeText = time
    )
}

fun TaskEntity.TaskItem.domain(): TasksDomain.AgentTask {

    val candidate = TasksDomain.Candidate(
        lastName = candidate?.lastName,
        businessId = candidate?.businessId,
        createdAt = candidate?.createdAt,
        firstName = candidate?.firstName,
        id = candidate?.id,
        lastModifiedAt = candidate?.lastModifiedAt,
        mobile = candidate?.mobile,
        photo = candidate?.photo
    )

    return TasksDomain.AgentTask(
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
        id = taskId,
        verificationType = verificationType
    )
}