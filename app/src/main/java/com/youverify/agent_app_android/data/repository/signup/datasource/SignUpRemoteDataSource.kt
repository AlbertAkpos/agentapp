package com.youverify.agent_app_android.data.repository.signup.datasource

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.signup.SignUpRequest

interface SignUpRemoteDataSource {

    suspend fun signUp(signUpRequest : SignUpRequest): Result<*>
}