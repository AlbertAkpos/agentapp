package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import kotlinx.coroutines.flow.Flow

//here we define abstract functions that represent each use-case in the project

interface StateLgaRepository {
    suspend fun getStateLgas(state : String) : List<String>?
}