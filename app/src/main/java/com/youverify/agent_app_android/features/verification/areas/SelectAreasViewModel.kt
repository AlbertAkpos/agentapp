package com.youverify.agent_app_android.features.verification.areas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreasResponse
import com.youverify.agent_app_android.domain.usecase.GetStateLgasUseCase
import com.youverify.agent_app_android.domain.usecase.PrefAreasUseCase
import com.youverify.agent_app_android.features.signup.SignUpViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectAreasViewModel @Inject constructor(
    private val getStateLgasUseCase: GetStateLgasUseCase,
    private val prefAreasUseCase: PrefAreasUseCase
): ViewModel() {


    fun getStateLgas(state : String) = liveData {
        val stateLgaList = getStateLgasUseCase.invoke(state)
        val stateLgas : ArrayList<String> = arrayListOf()
        if (!stateLgaList.isNullOrEmpty()){
            stateLgas.addAll(stateLgaList)
            emit(stateLgas)
        }
    }


    private val _prefAreasChannel = Channel<PrefAreasViewState>()
    val prefAreasChannel = _prefAreasChannel.receiveAsFlow()

    fun saveAreas(prefAreaRequest: PrefAreaRequest){
        viewModelScope.launch{
            _prefAreasChannel.send(PrefAreasViewState.Loading(R.string.preferred_areas_saved))

            prefAreasUseCase.invoke(prefAreaRequest).collect {
                when(it){
                    is Result.Success -> {
                        if (it.data is PrefAreasResponse){
                            _prefAreasChannel.send(PrefAreasViewState.Success(R.string.preferred_areas_saved, it.data.data))
                        }
                    }
                    is Result.Failed -> {
                        _prefAreasChannel.send(PrefAreasViewState.Failure(R.string.preferred_areas_saved, it.errorMessage.toString()))
                    }
                    else -> {}
                }
            }
        }
    }
}