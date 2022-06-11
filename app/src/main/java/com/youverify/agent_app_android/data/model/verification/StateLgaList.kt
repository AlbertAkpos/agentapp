package com.youverify.agent_app_android.data.model.verification


import com.google.gson.annotations.SerializedName

data class StateLgaList(
    @SerializedName("data")
    val `data`: List<String>
)