package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Part

interface UploadRepository {
    fun uploadImage(@Part uploadRequest: MultipartBody.Part) : Flow<Result<Any>>
}