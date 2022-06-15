package com.youverify.agent_app_android.data.model.verification.id

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerifyIDRequest(
    val firstName : String,
    val lastName: String,
    val type : String,
    val dateOfBirth : String,
    val reference : String,
    var imageUrl : String
): Parcelable