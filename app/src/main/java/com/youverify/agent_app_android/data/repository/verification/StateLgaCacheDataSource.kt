package com.youverify.agent_app_android.data.repository.verification

interface StateLgaCacheDataSource {
    suspend fun getStateLgasFromCache(): List<String>
    suspend fun saveStateLgasToCache(states: List<String>)
}