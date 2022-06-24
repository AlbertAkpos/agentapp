package com.youverify.agent_app_android.data.api

import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest
import com.youverify.agent_app_android.data.model.profile.ChangePassResponse
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.data.model.resetpassword.ResetPassResponse
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreasResponse
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenResponse
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
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
    suspend fun verifyId(@Body verifyIDRequest: VerifyIDRequest): Response<UploadImageResponse>

    @PUT("agents/me/areas")
    suspend fun saveAreas(@Body prefAreaRequest: PrefAreaRequest):  Response<PrefAreasResponse>

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

    @POST("auth/refresh")
    suspend fun refreshToken(@Body tokenRequest: TokenRequest): Response<TokenResponse>

    @POST("auth/change-password")
    suspend fun changePassword(@Body changePassRequest: ChangePassRequest): Response<ChangePassResponse>
}