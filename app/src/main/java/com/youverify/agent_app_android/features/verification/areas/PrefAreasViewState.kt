package com.youverify.agent_app_android.features.verification.areas

import com.youverify.agent_app_android.data.model.verification.areas.PrefAreasResponseData

sealed class PrefAreasViewState {
    class Success(val message: Int, val prefAreasResponseData: PrefAreasResponseData? = null) : PrefAreasViewState()
    class Failure(val message: Int, val errorMessage: String = "") : PrefAreasViewState()
    class Loading(val message: Int? = null) : PrefAreasViewState()
    object Empty : PrefAreasViewState()
}