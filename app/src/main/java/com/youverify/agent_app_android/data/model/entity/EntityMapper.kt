package com.youverify.agent_app_android.data.model.entity

import com.youverify.agent_app_android.data.model.tasks.TasksDomain

fun TasksDomain.AgentTask.toEntity(): TaskEntity.TaskItem {
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