package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest
import kotlinx.coroutines.flow.Flow

interface ChangePassRepository {

    fun changePassword(changePassRequest: ChangePassRequest) : Flow<Result<Any>>
}