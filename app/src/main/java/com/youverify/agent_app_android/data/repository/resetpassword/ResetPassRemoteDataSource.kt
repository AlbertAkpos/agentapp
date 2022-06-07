package com.youverify.agent_app_android.data.repository.resetpassword

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.resetpassword.Email

interface ResetPassRemoteDataSource {

    suspend fun sendResetEmail(email: Email) : Result<*>
}