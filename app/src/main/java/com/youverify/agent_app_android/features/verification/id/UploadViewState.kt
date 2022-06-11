package com.youverify.agent_app_android.features.verification.id

import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse

sealed class UploadViewState {
    class Success(val message: Int, val uploadResponse: UploadImageResponse? = null) : UploadViewState()
    class Failure(val message: Int, val errorMessage: String = "") : UploadViewState()
    class Loading(val message: Int? = null) : UploadViewState()
    object Empty : UploadViewState()
}