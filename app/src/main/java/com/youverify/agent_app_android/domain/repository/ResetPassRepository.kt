package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.resetpassword.Email
import kotlinx.coroutines.flow.Flow

interface ResetPassRepository {

    fun sendResetEmail(email: Email) : Flow<Result<Any>>
}