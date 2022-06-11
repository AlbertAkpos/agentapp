package com.youverify.agent_app_android.data.repository.signup

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AddressTaskService
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.data.model.signup.StatesList
import retrofit2.Response
import javax.inject.Inject

class StateCacheDataSourceImpl @Inject constructor(): StateCacheDataSource {
    //cache
    private var stateList = ArrayList<State>()

    override suspend fun getAllStatesFromCache(): List<State> {
        return stateList
    }

    override suspend fun saveStatesToCache(states: List<State>) {
        stateList.clear()
        stateList = ArrayList(states)
    }
}