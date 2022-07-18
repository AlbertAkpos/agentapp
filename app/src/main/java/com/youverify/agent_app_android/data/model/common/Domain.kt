package com.youverify.agent_app_android.data.model.common

import com.google.gson.annotations.SerializedName

object Domain {
    data class GenericResponse(
        val success: Boolean,
        val statusCode: Int?,
        val message: String?,
    )

    data class UpdateAgentResponse(
        val success: Boolean,
        val message: String?,
        val status: String?
    )

    data class AgentTasksAnalyticsResponse(
        val success: Boolean,
        val message: String,
        val analyticsData: AgentTasksAnalyticsData?
    )

    data class AgentTasksAnalyticsData(
        val completed: Int?,
        val queried: Int?,
        val declined: Int?,
        val completionTimeInMilliseconds: Double?,
        val percentageInTat: Int?,
        val percentageOutOf: Int?
    )
}