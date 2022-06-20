package com.youverify.agent_app_android.data.model.verification.upload


import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("data")
    val `data`: List<UploadImageData>?,
    @SerializedName("message")
    val message: String
)