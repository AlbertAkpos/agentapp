package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.base.FlowUseCase
import com.youverify.agent_app_android.domain.repository.UploadRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadUseCase @Inject constructor(
    private val uploadRepository: UploadRepository
):FlowUseCase<MultipartBody.Part, Any> {

    override fun invoke(params: MultipartBody.Part?): Flow<Result<Any>> {
        return uploadRepository.uploadImage(uploadRequest = params!!)
    }
}