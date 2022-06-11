package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.domain.repository.ResetPassRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPassUseCase @Inject constructor(
    private val resetPassRepository: ResetPassRepository)
    : FlowUseCase<Email, Any>{

    override fun invoke(params: Email?): Flow<Result<Any>> {
        return resetPassRepository.sendResetEmail(email = params!!)
    }
}