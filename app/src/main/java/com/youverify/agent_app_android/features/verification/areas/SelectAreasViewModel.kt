package com.youverify.agent_app_android.features.verification.areas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.model.verification.StateLgaList
import com.youverify.agent_app_android.domain.usecase.GetStateLgasUseCase
import com.youverify.agent_app_android.features.signup.SignUpViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectAreasViewModel @Inject constructor(
    private val getStateLgasUseCase: GetStateLgasUseCase
): ViewModel() {


    fun getStateLgas(state : String) = liveData {
        val stateLgaList = getStateLgasUseCase.invoke(state)
        val stateLgas : ArrayList<String> = arrayListOf()
        if (!stateLgaList.isNullOrEmpty()){
            stateLgas.addAll(stateLgaList)
            emit(stateLgas)
        }
    }
}