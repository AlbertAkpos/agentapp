package com.youverify.agent_app_android.data.model.login


import com.google.gson.annotations.SerializedName

data class LoginResponseData(
    @SerializedName("agent")
    val agent: Agent,
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)