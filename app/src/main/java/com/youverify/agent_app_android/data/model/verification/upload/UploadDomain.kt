package com.youverify.agent_app_android.data.model.verification.upload

object UploadDomain {
    data class UploadResponse(
        val urls: List<String>?,
        val message: String?,
        val success: Boolean
    )
}