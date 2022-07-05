package com.youverify.agent_app_android.data.model.verification.areas


import com.google.gson.annotations.SerializedName

data class PrefAreasResponseData(
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
    @SerializedName("id")
    val id: String,
    @SerializedName("isTrained")
    val isTrained: Boolean,
    @SerializedName("isVerified")
    val isVerified: Boolean,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("location")
    val location: Any,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("photo")
    val photo: Any,
    @SerializedName("preferredAreas")
    val preferredAreas: List<String>,
    @SerializedName("stateOfResidence")
    val stateOfResidence: String,
)