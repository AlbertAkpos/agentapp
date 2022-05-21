package com.youverify.agent_app_android.features.login

import com.youverify.agent_app_android.data.model.login.LoginResponseData

sealed class LoginViewState {
    class Success(val message: Int, val loginResponseData: LoginResponseData? = null) : LoginViewState()
    class Failure(val message: Int, val errorMessage: String = "") : LoginViewState()
    class Loading(val message: Int? = null) : LoginViewState()
    object Empty : LoginViewState()
}