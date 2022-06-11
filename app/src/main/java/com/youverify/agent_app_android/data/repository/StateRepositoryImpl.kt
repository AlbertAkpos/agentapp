package com.youverify.agent_app_android.data.repository

import android.util.Log
import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.data.repository.signup.StateCacheDataSource
import com.youverify.agent_app_android.data.repository.signup.StateRemoteDataSource
import com.youverify.agent_app_android.domain.repository.StateRepository
import javax.inject.Inject


class StateRepositoryImpl @Inject constructor(
    private val stateRemoteDataSource: StateRemoteDataSource,
    private val stateCacheDataSource: StateCacheDataSource
    ): StateRepository {

    override suspend fun getAllStates(): List<State>{
        return getStatesFromCache()
    }

    private suspend fun getStatesFromApi() : List<State>{
        lateinit var stateList : List<State>

        //get states from API
        try {
            val response = stateRemoteDataSource.getAllStates()
            val body = response.body()

            if (body != null){
                stateList = body.data
            }
        }catch (ex: Exception){
            Log.i("Error: ", ex.message.toString())
            stateList = emptyList()
        }

        return stateList
    }


    //function to getMovies from cache
    private suspend fun getStatesFromCache(): List<State>{
        lateinit var stateList: List<State>

        //get data from the API and
        //assign movies taken from the api to this list and return it.
        try{
            stateList = stateCacheDataSource.getAllStatesFromCache()
        }catch (ex: Exception){
            Log.i("MyTag", ex.message.toString())
        }

        //check movieList
        if (stateList.isNotEmpty()){  //means there are movie data available, just return it
            return stateList
        }else{  //we fetch movies from database and save to cache
            stateList = getStatesFromApi()
            stateCacheDataSource.saveStatesToCache(stateList)
        }

        return stateList
    }
}