package com.youverify.agent_app_android.data.repository.signup

import com.youverify.agent_app_android.data.model.signup.StatesList
import retrofit2.Response

interface StateRemoteDataSource {
    suspend fun getAllStates(): Response<StatesList>
}