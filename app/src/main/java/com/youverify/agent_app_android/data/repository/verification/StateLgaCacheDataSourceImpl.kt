package com.youverify.agent_app_android.data.repository.verification

import com.youverify.agent_app_android.core.functional.Failure
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.AddressTaskService
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.data.model.signup.StatesList
import retrofit2.Response
import javax.inject.Inject

class StateLgaCacheDataSourceImpl @Inject constructor(): StateLgaCacheDataSource {
    //cache
    private var stateList = ArrayList<String>()

    override suspend fun getStateLgasFromCache(): List<String> {
        return stateList
    }

    override suspend fun saveStateLgasToCache(states: List<String>) {
        stateList.clear()
        stateList = ArrayList(states)
    }
}