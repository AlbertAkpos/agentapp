package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.model.signup.SignUpResponseData
import com.youverify.agent_app_android.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SignupUseCase @Inject constructor (
    private val signUpRepository: SignUpRepository)
    : FlowUseCase<SignUpRequest, Any> {

    //execute function of the getMovies use case from the repository
//    suspend fun execute(signUpRequest : SignUpRequest) : SignUpResponseData? = signUpRepository.signUp(signUpRequest)

    override fun invoke(params: SignUpRequest?): Flow<Result<Any>> {
        return signUpRepository.signUp(params!!)
    }
}