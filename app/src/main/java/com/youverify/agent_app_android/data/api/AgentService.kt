package com.youverify.agent_app_android.data.api

import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.data.model.resetpassword.ResetPassResponse
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import com.youverify.agent_app_android.data.model.verification.upload.VerifyIDRequest
import retrofit2.Response
import retrofit2.http.*

interface AgentService {

    @POST("agents")
    suspend fun signUpAgent(@Body body: SignUpRequest): Response<SignUpResponse?>

    @POST("auth/token")
    suspend fun loginAgent(@Body body: LoginRequest): Response<LoginResponse?>

    @POST("auth/reset-password")
    suspend fun sendResetEmail(@Body email: Email): Response<ResetPassResponse>

    @POST("agents/verify")
    suspend fun verifyAgentId(@Body verifyIDRequest: VerifyIDRequest): Response<UploadImageResponse>

    @GET("https://address-task.dev.svc.youverify.co/v1/agents/tasks/pending")
    suspend fun getAgentTasks(): TasksDto.AgentTasksResponse

    @PUT("https://address-task.dev.svc.youverify.co/v1/tasks/{taskId}/start")
    suspend fun startTask(@Path("taskId") taskId: String): TasksDto.StartTaskResponse

    @GET("https://address-task.dev.svc.youverify.co/v1/messages/reject")
    suspend fun getRejectionMessages(): TasksDto.RejectionMessagesResponse

    @GET("https://address-task.dev.svc.youverify.co/v1/messages/submit")
    suspend fun getSubmissionMessages(): TasksDto.SubmissionMessagesResponse

    @PUT("https://address-task.dev.svc.youverify.co/v1/tasks/{taskId}/submit")
    suspend fun submitTask(@Body request: TasksDto.SubmitTaskRequest, @Path("taskId") taskId: String): TasksDto.GenericResponse

    @PUT("https://address-task.dev.svc.youverify.co/v1/tasks/{taskId}/report")
    suspend fun updateTask(@Path("taskId") taskId: String, @Body updateTaskRequest: TasksDto.UpdateTaskRequest) : TasksDto.GenericResponse

}