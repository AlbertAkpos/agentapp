package com.youverify.agent_app_android.data.model.verification.refresh_token

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("agentId")
    val agentId: String
)