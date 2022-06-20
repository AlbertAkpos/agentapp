package com.youverify.agent_app_android.util.helper

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Multipart
import java.io.File

fun createMultipart(file: File, mimetype: String = "image/png"): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        "files",
        file.name,
        file.asRequestBody(mimetype.toMediaTypeOrNull())
    )
}