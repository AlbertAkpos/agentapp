package com.youverify.agent_app_android.data.api

import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {

    @Multipart
    @POST("uploads/media")
    suspend fun uploadImage(@Part files: MultipartBody.Part) : Response<UploadImageResponse>
}