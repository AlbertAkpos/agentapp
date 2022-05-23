package com.youverify.agent_app_android.data.model.login


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val `data`: LoginResponseData,
    @SerializedName("links")
    val links: List<Any>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
)