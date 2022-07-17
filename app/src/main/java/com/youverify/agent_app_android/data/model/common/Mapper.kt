package com.youverify.agent_app_android.data.model.common

fun Dto.GenericResponse.map(): Domain.GenericResponse {
    return Domain.GenericResponse(success = success == true, statusCode, message)
}

fun Dto.UpdateAgentStatusResponse.map(): Domain.UpdateAgentResponse = Domain.UpdateAgentResponse(
    success = success == true,
    message = message,
    status = agent?.visibilityStatus
)

fun Dto.AgentTasksAnalyticsResponse.map(): Domain.AgentTasksAnalyticsResponse {
    val anaylyticsData = Domain.AgentTasksAnalyticsData(
        completed = data?.completed,
        queried = data?.queried,
        declined = data?.declined,
        percentageInTat = data?.percentageInTat,
        percentageOutOf = data?.percentageOutOf,
        completionTimeInMilliseconds = data?.completionTimeInMilliseconds
    )
    return Domain.AgentTasksAnalyticsResponse(
        success = success == true,
        message = message ?: "",
        analyticsData = anaylyticsData
    )
}