package com.youverify.agent_app_android.data.repository.verification.refresh_token

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest

interface TokenDataSource {
    suspend fun refreshToken(tokenRequest: TokenRequest) : Result<*>
}