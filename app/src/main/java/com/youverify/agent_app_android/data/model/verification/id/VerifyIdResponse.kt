package com.youverify.agent_app_android.data.model.verification.id


import com.google.gson.annotations.SerializedName

data class VerifyIdResponse(
    @SerializedName("data")
    val data: VerifyIdResponseData,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
)