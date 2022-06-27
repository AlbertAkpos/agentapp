package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest
import com.youverify.agent_app_android.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TokenUseCase @Inject constructor (
    private val tokenRepository: TokenRepository)
    : FlowUseCase<TokenRequest, Any> {

    override fun invoke(params: TokenRequest?): Flow<Result<Any>> {
        return tokenRepository.refreshToken(params!!)
    }
}