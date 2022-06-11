package com.youverify.agent_app_android.data.repository.verification.upload

import com.youverify.agent_app_android.core.functional.Result
import okhttp3.MultipartBody
import retrofit2.http.Part

interface UploadRemoteDataSource {
    suspend fun uploadImage(@Part uploadRequest: MultipartBody.Part): Result<*>
}