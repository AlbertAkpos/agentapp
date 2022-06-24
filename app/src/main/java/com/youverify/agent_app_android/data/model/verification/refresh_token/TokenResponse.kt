package com.youverify.agent_app_android.data.model.verification.refresh_token


import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("data")
    val `data`: TokenResponseData,
    @SerializedName("links")
    val links: List<Any>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
)