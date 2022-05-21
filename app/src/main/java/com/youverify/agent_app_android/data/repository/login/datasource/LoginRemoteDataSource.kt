package com.youverify.agent_app_android.data.repository.login.datasource

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.login.LoginRequest


interface LoginRemoteDataSource {

    suspend fun login(loginRequest : LoginRequest): Result<*>
}