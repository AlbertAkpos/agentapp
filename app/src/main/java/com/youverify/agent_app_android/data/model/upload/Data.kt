package com.youverify.agent_app_android.data.model.upload


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("cachedLocation")
    val cachedLocation: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("mimetype")
    val mimetype: String,
    @SerializedName("originalname")
    val originalname: String,
    @SerializedName("sizeInByte")
    val sizeInByte: Int
)