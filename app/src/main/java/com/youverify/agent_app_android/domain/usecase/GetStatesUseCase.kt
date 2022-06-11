package com.youverify.agent_app_android.domain.usecase

import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.domain.repository.StateRepository
import javax.inject.Inject

class GetStatesUseCase @Inject constructor(private val stateRepository: StateRepository) {
    suspend fun execute(): List<State>?  = stateRepository.getAllStates()
}