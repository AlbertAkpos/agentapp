package com.youverify.agent_app_android.domain.repository

import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.State
import kotlinx.coroutines.flow.Flow

//here we define abstract functions that represent each use-case in the project

interface StateRepository {
    suspend fun getAllStates() : List<State>?
}