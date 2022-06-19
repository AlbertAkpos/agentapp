package com.youverify.agent_app_android.data.api

import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.data.model.resetpassword.ResetPassResponse
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreasResponse
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.data.model.verification.id.VerifyIdResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AgentService {

    @POST("agents")
    suspend fun signUpAgent(@Body body : SignUpRequest) : Response<SignUpResponse?>

    @POST("auth/token")
    suspend fun loginAgent(@Body body: LoginRequest) : Response<LoginResponse?>

    @POST("auth/reset-password")
    suspend fun sendResetEmail(@Body email: Email) : Response<ResetPassResponse>

    @POST("agents/verify")
    suspend fun verifyId(@Body verifyIDRequest: VerifyIDRequest): Response<VerifyIdResponse>

    @PUT("agents/me/areas")
    suspend fun saveAreas(@Body prefAreaRequest: PrefAreaRequest): Response<PrefAreasResponse>
}