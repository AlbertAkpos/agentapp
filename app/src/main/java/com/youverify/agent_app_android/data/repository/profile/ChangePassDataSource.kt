package com.youverify.agent_app_android.data.repository.profile

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest


interface ChangePassDataSource {

    suspend fun changePassword(changePassRequest: ChangePassRequest): Result<*>
}