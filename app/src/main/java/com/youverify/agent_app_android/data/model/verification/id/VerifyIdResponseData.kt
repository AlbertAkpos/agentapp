package com.youverify.agent_app_android.data.model.verification.id


import com.google.gson.annotations.SerializedName

data class VerifyIdResponseData(
    @SerializedName("isVerified")
    val isVerified: Boolean
)