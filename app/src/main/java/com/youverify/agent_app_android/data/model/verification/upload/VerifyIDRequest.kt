package com.youverify.agent_app_android.data.model.verification.upload

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerifyIDRequest(
    val firstName : String,
    val lastName: String,
    val idType : String,
    val dateOfBirth : String,
    val reference : String,
    val imageUrl : String
): Parcelable