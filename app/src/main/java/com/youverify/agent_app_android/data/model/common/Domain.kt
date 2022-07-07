package com.youverify.agent_app_android.data.model.common

object Domain {
    data class GenericResponse(
        val success: Boolean,
        val statusCode: Int?,
        val message: String?,
    )

    data class UpdateAgentResponse(
        val success: Boolean,
        val message: String?,
        val status: String?
    )
}