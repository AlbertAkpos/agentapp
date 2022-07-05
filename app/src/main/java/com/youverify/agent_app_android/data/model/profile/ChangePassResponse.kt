package com.youverify.agent_app_android.data.model.profile


import com.google.gson.annotations.SerializedName

data class ChangePassResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
)