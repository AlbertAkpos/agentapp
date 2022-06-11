package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.data.model.resetpassword.ResetPassResponse
import com.youverify.agent_app_android.data.repository.resetpassword.ResetPassRemoteDataSource
import com.youverify.agent_app_android.domain.repository.ResetPassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ResetPassRepositoryImpl @Inject constructor(
    private val resetPassRemoteDataSource: ResetPassRemoteDataSource
) : ResetPassRepository {

    override fun sendResetEmail(email: Email): Flow<Result<Any>> {
        return flow {
            when (val res = resetPassRemoteDataSource.sendResetEmail(email)) {
                is Result.Success<*> -> {
                    if (res.data is ResetPassResponse){
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