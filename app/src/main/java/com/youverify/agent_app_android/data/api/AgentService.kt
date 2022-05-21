package com.youverify.agent_app_android.data.api

import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AgentService {

    @POST("agents")
    suspend fun signUpAgent(@Body body : SignUpRequest) : Response<SignUpResponse?>

    @POST("auth/token")
    suspend fun loginAgent(@Body body: LoginRequest) : Response<LoginResponse?>
}