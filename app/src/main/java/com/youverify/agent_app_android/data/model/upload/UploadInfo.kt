package com.youverify.agent_app_android.data.model.upload

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadInfo(
    val firstName : String,
    val lastName: String,
    val idType : String,
    val dateOfBirth : String,
    val reference : String,
    val image : String
): Parcelable