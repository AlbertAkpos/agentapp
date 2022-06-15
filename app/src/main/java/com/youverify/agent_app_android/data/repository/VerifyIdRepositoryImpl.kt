package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreasResponse
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.data.model.verification.id.VerifyIdResponse
import com.youverify.agent_app_android.data.repository.verification.areas.PrefAreasRemoteDataSource
import com.youverify.agent_app_android.data.repository.verification.id.VerifyIdRemoteDataSource
import com.youverify.agent_app_android.domain.repository.PrefAreasRepository
import com.youverify.agent_app_android.domain.repository.VerifyIdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyIdRepositoryImpl @Inject constructor(
    private val verifyIdRemoteDataSource: VerifyIdRemoteDataSource)
    : VerifyIdRepository{

    override fun verifyId(verifyIDRequest: VerifyIDRequest, token: String): Flow<Result<Any>> {
        return flow {
            when (val res = verifyIdRemoteDataSource.verifyId(verifyIDRequest, token)) {

                is Result.Success<*> -> {
                    if (res.data is VerifyIdResponse) {
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