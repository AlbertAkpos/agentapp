package com.youverify.agent_app_android.features.signup

import com.youverify.agent_app_android.data.model.signup.SignUpResponseData

sealed class SignUpViewState {
    class Success(val message: Int, val signUpResponseData: SignUpResponseData? = null) : SignUpViewState()
    class Failure(val message: Int, val errorMessage: String = "") : SignUpViewState()
    class Loading(val message: Int? = null) : SignUpViewState()
    object Empty : SignUpViewState()
}