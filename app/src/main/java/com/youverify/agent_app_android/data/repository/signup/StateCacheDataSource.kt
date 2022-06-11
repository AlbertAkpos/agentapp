package com.youverify.agent_app_android.data.repository.signup

import com.youverify.agent_app_android.data.model.signup.State

interface StateCacheDataSource {
    suspend fun getAllStatesFromCache(): List<State>
    suspend fun saveStatesToCache(states: List<State>)
}