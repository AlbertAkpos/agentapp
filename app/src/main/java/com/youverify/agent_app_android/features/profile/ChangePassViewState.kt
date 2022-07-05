package com.youverify.agent_app_android.features.profile

import com.youverify.agent_app_android.data.model.profile.ChangePassResponse

sealed class ChangePassViewState {
    class Success(val message: Int, val changePassResponse: ChangePassResponse? = null) : ChangePassViewState()
    class Failure(val message: Int, val errorMessage: String = "") : ChangePassViewState()
    class Loading(val message: Int? = null) : ChangePassViewState()
    object Empty : ChangePassViewState()
}