package com.youverify.agent_app_android.data.model.signup


import com.google.gson.annotations.SerializedName

data class StatesList(
    @SerializedName("data")
    val `data`: List<State>
)