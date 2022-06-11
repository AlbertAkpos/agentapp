package com.youverify.agent_app_android.data.repository.verification

import com.youverify.agent_app_android.data.model.verification.StateLgaList
import retrofit2.Response

interface StateLgaRemoteDataSource {
    suspend fun getStateLgas(state: String): Response<StateLgaList>
}