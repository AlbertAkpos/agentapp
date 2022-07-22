package com.youverify.agent_app_android.data.model.login


import com.google.gson.annotations.SerializedName

data class Agent(
    @SerializedName("agentStatus")
    val agentStatus: String,
    @SerializedName("emailAddress")
    val emailAddress: String,
    @SerializedName("fcmToken")
    val fcmToken: Any,
    @SerializedName("fieldPartnerId")
    val fieldPartnerId: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("hasVerifiedEmail")
    val hasVerifiedEmail: Boolean,
    @SerializedName("isTrained")
    val isTrained: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("isVerified")
    val isVerified: Boolean,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("photo")
    val photo: String?,
    @SerializedName("preferredAreas")
    val preferredAreas: List<String>,
    @SerializedName("settings")
    val settings: Settings,
    @SerializedName("stateOfResidence")
    val stateOfResidence: String,
    @SerializedName("visibilityStatus")
    val visibilityStatus: String
)