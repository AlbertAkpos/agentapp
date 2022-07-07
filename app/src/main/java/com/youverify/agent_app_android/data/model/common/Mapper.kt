package com.youverify.agent_app_android.data.model.common

fun Dto.GenericResponse.map(): Domain.GenericResponse {
    return Domain.GenericResponse(success = success == true, statusCode, message)
}

fun Dto.UpdateAgentStatusResponse.map(): Domain.UpdateAgentResponse = Domain.UpdateAgentResponse(
    success = success == true,
    message = message,
    status = agent?.visibilityStatus
)