package com.youverify.agent_app_android.data.model.signup


import com.google.gson.annotations.SerializedName

data class SignUpResponseData(

    @SerializedName("id")
    val id: String,

    @SerializedName("agentStatus")
    val agentStatus: String,

    @SerializedName("emailAddress")
    val emailAddress: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("preferredAreas")
    val preferredAreas: List<Any>,

    @SerializedName("stateOfResidence")
    val stateOfResidence: String,

    @SerializedName("visibilityStatus")
    val visibilityStatus: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("fieldPartnerId")
    val fieldPartnerId: String,

    @SerializedName("hasVerifiedEmail")
    val hasVerifiedEmail: Boolean,

    @SerializedName("isVerified")
    val isVerified: Boolean,

    @SerializedName("fcmToken")
    val fcmToken: Any





//    @SerializedName("lastLogin")
//    val lastLogin: Any,
//    @SerializedName("lastModifiedAt")
//    val lastModifiedAt: String,
//
//    @SerializedName("photo")
//    val photo: Any,
//
//    @SerializedName("teamIds")
//    val teamIds: List<Any>,
//    @SerializedName("verificationAttempts")
//    val verificationAttempts: List<Any>,

)