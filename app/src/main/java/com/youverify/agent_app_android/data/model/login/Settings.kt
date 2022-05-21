package com.youverify.agent_app_android.data.model.login


import com.google.gson.annotations.SerializedName

data class Settings(
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("sessionTimeoutInMinutes")
    val sessionTimeoutInMinutes: Int,
    @SerializedName("timeFormat")
    val timeFormat: String,
    @SerializedName("timezone")
    val timezone: String
)