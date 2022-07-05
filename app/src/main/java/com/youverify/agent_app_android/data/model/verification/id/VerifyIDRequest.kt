package com.youverify.agent_app_android.data.model.verification.id

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerifyIDRequest(
    var firstName : String,
    var lastName: String,
    var type : String?,
    var dateOfBirth : String?,
    var reference : String?,
    var imageUrl : String
): Parcelable