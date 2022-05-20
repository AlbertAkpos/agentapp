package com.youverify.agent_app_android.presentation.signup

import com.youverify.agent_app_android.data.model.signup.SignUpResponseData

sealed class SignUpViewState {
    class Success(val message: Int, val signUpResponse: SignUpResponseData? = null) : SignUpViewState()
    class Failure(val message: Int, val errorMessage: String = "") : SignUpViewState()
    class Loading(val message: Int? = null) : SignUpViewState()
    object Empty : SignUpViewState()
}