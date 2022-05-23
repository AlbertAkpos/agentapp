package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository)
    : FlowUseCase<LoginRequest, Any> {

    override fun invoke(params: LoginRequest?): Flow<Result<Any>> {
        return loginRepository.login(loginRequest = params!!)
    }
}