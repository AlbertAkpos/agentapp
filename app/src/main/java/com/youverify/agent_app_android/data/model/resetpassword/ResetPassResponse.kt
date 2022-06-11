package com.youverify.agent_app_android.data.model.resetpassword


import com.google.gson.annotations.SerializedName

data class ResetPassResponse(
    @SerializedName("data")
    val `data`: ResetResponseData,
    @SerializedName("links")
    val links: List<Any>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
)