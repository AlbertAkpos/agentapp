package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenResponse
import com.youverify.agent_app_android.data.repository.verification.refresh_token.TokenDataSource
import com.youverify.agent_app_android.domain.repository.TokenRepository
import com.youverify.agent_app_android.core.functional.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository{
    override fun refreshToken(tokenRequest: TokenRequest): Flow<Result<Any>> {
        return flow {
            when (val res = tokenDataSource.refreshToken(tokenRequest)){
                is Result.Success<*> -> {
                    if (res.data is TokenResponse) {
                        emit(Result.Success(res.data))
                    }
                }
                is Result.Failed<*> -> {
                    emit(Result.Failed(res.errorMessage!!))
                }
                else -> {}
            }
        }
    }
}