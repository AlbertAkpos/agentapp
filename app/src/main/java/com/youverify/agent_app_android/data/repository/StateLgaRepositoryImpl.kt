package com.youverify.agent_app_android.data.repository

import android.util.Log
import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.data.repository.verification.StateLgaCacheDataSource
import com.youverify.agent_app_android.data.repository.verification.StateLgaRemoteDataSource
import com.youverify.agent_app_android.domain.repository.StateLgaRepository
import javax.inject.Inject


class StateLgaRepositoryImpl @Inject constructor(
    private val stateLgaRemoteDataSource: StateLgaRemoteDataSource,
    private val stateLgaCacheDataSource: StateLgaCacheDataSource
    ): StateLgaRepository {

    override suspend fun getStateLgas(state: String): List<String> {
       return getStateLgaFromCache(state)
    }

    private suspend fun getStatesFromApi(state: String): List<String>{
        lateinit var stateLgaList : List<String>

        //get states from API
        try {
            val response = stateLgaRemoteDataSource.getStateLgas(state)
            val body = response.body()

            if (body != null){
                stateLgaList = body.data
            }
        }catch (ex: Exception){
            Log.i("Error: ", ex.message.toString())
        }

        return stateLgaList
    }

    private suspend fun getStateLgaFromCache(state: String): List<String>{
        lateinit var stateLgaList: List<String>

        //get data from the API and
        //assign movies taken from the api to this list and return it.
        try{
            stateLgaList = stateLgaCacheDataSource.getStateLgasFromCache()
        }catch (ex: Exception){
            Log.i("MyTag", ex.message.toString())
        }

        //check movieList
        if (stateLgaList.isNotEmpty()){  //means there are movie data available, just return it
            return stateLgaList
        }else{  //we fetch movies from database and save to cache
            stateLgaList = getStatesFromApi(state)
            stateLgaCacheDataSource.saveStateLgasToCache(stateLgaList)
        }

        return stateLgaList
    }


}