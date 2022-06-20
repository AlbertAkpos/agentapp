package com.youverify.agent_app_android.features.verification.id

import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import java.io.File

sealed class UploadViewState {
    class Success(val message: Int, val uploadResponse: UploadImageResponse? = null, val file: File? = null) : UploadViewState()
    class Failure(val message: Int, val errorMessage: String = "", val file: File? = null) : UploadViewState()
    class Loading(val message: Int? = null) : UploadViewState()
    object Empty : UploadViewState()
}