package com.youverify.agent_app_android.data.model.common

import com.google.gson.annotations.SerializedName

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
}