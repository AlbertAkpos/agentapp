package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.repository.signup.SignUpRemoteDataSource
import com.youverify.agent_app_android.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SignUpRepositoryImpl @Inject constructor(
    private val signUpRemoteDataSource: SignUpRemoteDataSource
    ): SignUpRepository{

    override fun signUp(signUpRequest: SignUpRequest): Flow<Result<Any>> {
        return flow {
            when (val res = signUpRemoteDataSource.signUp(signUpRequest)) {

                is Result.Success<*> -> {
                    if (res.data is SignUpResponse) {
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