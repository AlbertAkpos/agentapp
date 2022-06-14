package com.youverify.agent_app_android.features.task

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.youverify.agent_app_android.data.model.TaskItem
import com.youverify.agent_app_android.data.model.tasks.TasksDomain

@Keep
data class TaskBundle(
    @SerializedName("taskItem")
    val taskItem: TasksDomain.AgentTask? = null
)
