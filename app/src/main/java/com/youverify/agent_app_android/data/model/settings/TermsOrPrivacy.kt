package com.youverify.agent_app_android.data.model.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TermsOrPrivacy(
    val isTerms: Boolean
):Parcelable