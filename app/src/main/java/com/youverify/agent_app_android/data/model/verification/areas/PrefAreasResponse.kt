package com.youverify.agent_app_android.data.model.verification.areas


import com.google.gson.annotations.SerializedName

data class PrefAreasResponse(
    @SerializedName("data")
    val `data`: PrefAreasResponseData,
)