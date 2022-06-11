package com.youverify.agent_app_android.data.api

import com.youverify.agent_app_android.data.model.verification.StateLgaList
import com.youverify.agent_app_android.data.model.signup.StatesList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressTaskService {
    @GET("states")
    suspend fun getAllStates() : Response<StatesList>

    @GET("states/{alias}/lgas")
    suspend fun getStateLgas(@Path("alias") state: String) : Response<StateLgaList>
}