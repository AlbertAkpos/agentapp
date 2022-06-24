package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun refreshToken(tokenRequest: TokenRequest) : Flow<Result<Any>>
}