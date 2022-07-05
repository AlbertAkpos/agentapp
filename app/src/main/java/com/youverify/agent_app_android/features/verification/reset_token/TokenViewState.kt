package com.youverify.agent_app_android.features.verification.reset_token

import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenResponse

sealed class TokenViewState {
    class Success(val message: Int, val tokenResponse: TokenResponse? = null) : TokenViewState()
    class Failure(val message: Int, val errorMessage: String = "") : TokenViewState()
    class Loading(val message: Int? = null) : TokenViewState()
    object Empty : TokenViewState()
}