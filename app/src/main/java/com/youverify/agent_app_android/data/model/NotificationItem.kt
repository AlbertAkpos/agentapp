package com.youverify.agent_app_android.data.model

import com.youverify.agent_app_android.data.model.tasks.TasksDomain

data class NotificationItem (
    val image: Int,
    val accessText: String,
    val nameText: String,
    val addressText: String,
    val timeText: String,
    val taskItem: TasksDomain.AgentTask? = null,
    val submitTask: TasksDomain.SubmitTask? = null
)