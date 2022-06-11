package com.youverify.agent_app_android.data.model.verification.upload


import com.google.gson.annotations.SerializedName

data class UploadImageData(
    @SerializedName("key")
    val key: String,
    @SerializedName("cachedLocation")
    val cachedLocation: String,
    @SerializedName("location")
    val location: String,
)