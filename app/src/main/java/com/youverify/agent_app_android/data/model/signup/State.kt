package com.youverify.agent_app_android.data.model.signup


import com.google.gson.annotations.SerializedName

data class State(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("name")
    val name: String
)