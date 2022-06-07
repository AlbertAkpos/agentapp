package com.youverify.agent_app_android.data.model.upload


import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("links")
    val links: List<Any>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
)