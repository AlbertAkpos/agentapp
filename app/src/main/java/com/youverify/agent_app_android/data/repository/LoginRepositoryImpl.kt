package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.repository.login.datasource.LoginRemoteDataSource
import com.youverify.agent_app_android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource)
    : LoginRepository {

    override fun login(loginRequest: LoginRequest): Flow<Result<Any>> {
        return flow {
            when (val res = loginRemoteDataSource.login(loginRequest)){
                is Result.Success<*> -> {
                    if (res.data is LoginResponse) {
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