package com.youverify.agent_app_android.data.repository.verification.upload

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.UploadService
import okhttp3.MultipartBody
import retrofit2.http.Part
import javax.inject.Inject

class UploadRemoteDataSourceImpl @Inject constructor(
    private val uploadService: UploadService
): UploadRemoteDataSource{

    override suspend fun uploadImage(@Part uploadRequest: MultipartBody.Part): Result<*> {
        return try{
            val res = uploadService.uploadImage(uploadRequest)

            when(res.isSuccessful){
                true -> {
                    res.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error(Failure.ServerError)
                }
                false ->{
                    Result.Failed(res.errorBody())
                }
            }
        }catch (e: Throwable){
            Result.Failed(e.message)
        }
    }
}