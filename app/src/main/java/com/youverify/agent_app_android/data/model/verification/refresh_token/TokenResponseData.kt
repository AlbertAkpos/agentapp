package com.youverify.agent_app_android.data.model.verification.refresh_token


import com.google.gson.annotations.SerializedName

data class TokenResponseData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)