package com.youverify.agent_app_android.data.repository.signup

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AddressTaskService
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.StatesList
import retrofit2.Response
import javax.inject.Inject

class StateRemoteDataSourceImpl @Inject constructor(
    private val addressTaskService: AddressTaskService
) : StateRemoteDataSource {

    override suspend fun getAllStates(): Response<StatesList> = addressTaskService.getAllStates()

}