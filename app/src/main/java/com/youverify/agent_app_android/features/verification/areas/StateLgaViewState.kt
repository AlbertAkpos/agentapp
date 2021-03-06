package com.youverify.agent_app_android.features.verification.areas

sealed class StateLgaViewState {
    class Success(val message: Int, val stateLgaList: List<String>? = null) : StateLgaViewState()
    class Failure(val message: Int, val errorMessage: String = "") : StateLgaViewState()
    class Loading(val message: Int? = null) : StateLgaViewState()
    object Empty : StateLgaViewState()
}