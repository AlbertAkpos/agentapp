package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest
import com.youverify.agent_app_android.data.model.profile.ChangePassResponse
import com.youverify.agent_app_android.data.repository.login.LoginRemoteDataSource
import com.youverify.agent_app_android.data.repository.profile.ChangePassDataSource
import com.youverify.agent_app_android.domain.repository.ChangePassRepository
import com.youverify.agent_app_android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePassRepositoryImpl @Inject constructor(
    private val changePassDataSource: ChangePassDataSource)
    : ChangePassRepository {

    override fun changePassword(changePassRequest: ChangePassRequest): Flow<Result<Any>> {
        return flow {
            when (val res = changePassDataSource.changePassword(changePassRequest)){
                is Result.Success<*> -> {
                    if (res.data is ChangePassResponse) {
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