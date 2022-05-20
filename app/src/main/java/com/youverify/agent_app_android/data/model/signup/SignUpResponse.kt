package com.youverify.agent_app_android.data.model.signup


import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("data")
    val `data`: SignUpResponseData,
    @SerializedName("links")
    val links: List<Any>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
)