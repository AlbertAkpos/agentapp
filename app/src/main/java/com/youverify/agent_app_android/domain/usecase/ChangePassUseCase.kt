package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest
import com.youverify.agent_app_android.domain.repository.ChangePassRepository
import com.youverify.agent_app_android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePassUseCase @Inject constructor(
    private val changePassRepository: ChangePassRepository)
    : FlowUseCase<ChangePassRequest, Any> {

    override fun invoke(params: ChangePassRequest?): Flow<Result<Any>> {
        return changePassRepository.changePassword(changePassRequest = params!!)
    }
}