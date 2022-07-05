package com.youverify.agent_app_android.features.verification.id

import com.youverify.agent_app_android.data.model.verification.id.VerifyIdResponse

sealed class VerifyIdViewState {
    class Success(val message: Int, val verifyIdResponse: VerifyIdResponse? = null) : VerifyIdViewState()
    class Failure(val message: Int, val errorMessage: String = "") : VerifyIdViewState()
    class Loading(val message: Int? = null) : VerifyIdViewState()
    object Empty : VerifyIdViewState()
}