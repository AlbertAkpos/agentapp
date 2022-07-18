package com.youverify.agent_app_android.data.model.common

import com.google.gson.annotations.SerializedName
import com.youverify.agent_app_android.data.model.login.Agent

object Dto {
    data class FcmToken(
        @SerializedName("fcmToken")
        val fcmToken: String
    )

    data class GenericResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode", alternate = ["status_code"]) val statusCode: Int?,
        @SerializedName("message") val message: String?,
    )

    data class UpdateAgentStatusResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode", alternate = ["status_code"]) val statusCode: Int?,
        @SerializedName("message") val message: String?,
        @SerializedName("data") val agent: Agent?
    )

    data class UpdateAgentStatusRequest(
        @SerializedName("status")
        /** Allowed values: Online, Offline */
        val status: String
    )

    data class AgentTasksAnalyticsResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode", alternate = ["status_code"]) val statusCode: Int?,
        @SerializedName("message") val message: String?,
        @SerializedName("data") val data: AgentTasksAnalytics?
    )

    data class AgentTasksAnalytics(
        @SerializedName("completed")
        val completed: Int?,
        @SerializedName("queried")
        val queried: Int?,
        @SerializedName("declined")
        val declined: Int?,
        @SerializedName("avgCompletionTimeInMs")
        val completionTimeInMilliseconds: Double?,
        @SerializedName("percentInTAT")
        val percentageInTat: Int?,
        @SerializedName("percentOutOfTAT")
        val percentageOutOf: Int?
    )

    data class AgentPerformanceAnalysisResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode", alternate = ["status_code"]) val statusCode: Int?,
        @SerializedName("message") val message: String?,
        @SerializedName("data") val data: AgentTasksAnalytics?
    )

    data class AgentPerformanceAnalysisData(
        @SerializedName("completed")
        val completed: Int?,
        @SerializedName("queried")
        val queried: Int?,
        @SerializedName("avgCompletionTimeInMs")
        val completionTimeInMilliseconds: Double?,
    )
}