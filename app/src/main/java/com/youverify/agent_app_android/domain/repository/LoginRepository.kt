package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.login.LoginRequest
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun login(loginRequest: LoginRequest) : Flow<Result<Any>>
}