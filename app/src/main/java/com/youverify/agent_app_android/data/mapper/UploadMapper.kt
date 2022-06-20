package com.youverify.agent_app_android.data.mapper

import com.youverify.agent_app_android.data.model.verification.upload.UploadDomain
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse

fun UploadImageResponse.map(): UploadDomain.UploadResponse {
    val fileUrls = data?.map { it.location }
    return UploadDomain.UploadResponse(
        urls = fileUrls,
        message = message
    )
}