package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreasResponse
import com.youverify.agent_app_android.data.repository.verification.areas.PrefAreasRemoteDataSource
import com.youverify.agent_app_android.domain.repository.PrefAreasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PrefAreasRepositoryImpl @Inject constructor(
    private val prefAreasRemoteDataSource: PrefAreasRemoteDataSource)
    : PrefAreasRepository{

    override fun saveAreas(prefAreaRequest: PrefAreaRequest, token: String): Flow<Result<Any>> {
        return flow {
            when (val res = prefAreasRemoteDataSource.saveAreas(prefAreaRequest, token)) {

                is Result.Success<*> -> {
                    if (res.data is PrefAreasResponse) {
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