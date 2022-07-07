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
}