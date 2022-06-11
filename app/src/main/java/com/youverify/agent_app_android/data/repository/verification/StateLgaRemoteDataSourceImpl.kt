package com.youverify.agent_app_android.data.repository.verification

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AddressTaskService
import com.youverify.agent_app_android.data.model.verification.StateLgaList
import retrofit2.Response
import javax.inject.Inject

class StateLgaRemoteDataSourceImpl @Inject constructor(
    private val addressTaskService: AddressTaskService
) : StateLgaRemoteDataSource {

    override suspend fun getStateLgas(state: String): Response<StateLgaList>  = addressTaskService.getStateLgas(state)
}