package com.youverify.agent_app_android.data.model.common

fun Dto.GenericResponse.map(): Domain.GenericResponse {
    return Domain.GenericResponse(success = success == true, statusCode, message)
}