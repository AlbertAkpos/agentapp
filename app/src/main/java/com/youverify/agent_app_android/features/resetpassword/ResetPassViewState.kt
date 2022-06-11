package com.youverify.agent_app_android.features.resetpassword

import com.youverify.agent_app_android.data.model.resetpassword.ResetPassResponse

sealed class ResetPassViewState {
    class Success(val message: Int, val resetPassResponse: ResetPassResponse? = null) : ResetPassViewState()
    class Failure(val message: Int, val errorMessage: String = "") : ResetPassViewState()
    class Loading(val message: Int? = null) : ResetPassViewState()
    object Empty : ResetPassViewState()
}