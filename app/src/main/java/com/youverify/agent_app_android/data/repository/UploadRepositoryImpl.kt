package com.youverify.agent_app_android.data.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.repository.upload.UploadRemoteDataSource
import com.youverify.agent_app_android.domain.repository.UploadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.http.Part
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val uploadRemoteDataSource: UploadRemoteDataSource
) : UploadRepository {

    override fun uploadImage(@Part uploadRequest: MultipartBody.Part): Flow<Result<Any>> {
        return flow {
            when (val res = uploadRemoteDataSource.uploadImage(uploadRequest)) {
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