package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SignupUseCase @Inject constructor (
    private val signUpRepository: SignUpRepository)
    : FlowUseCase<SignUpRequest, Any> {

    override fun invoke(params: SignUpRequest?): Flow<Result<Any>> {
        return signUpRepository.signUp(params!!)
    }
}