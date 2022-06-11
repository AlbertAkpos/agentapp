package com.youverify.agent_app_android.features.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponse
import com.youverify.agent_app_android.domain.usecase.GetStatesUseCase
import com.youverify.agent_app_android.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val yvAgentSignupUseCase: SignupUseCase,
    private val getAllStatesUseCase: GetStatesUseCase
    ): ViewModel(){


//    we  want Channel to hold the sign up state(loading, success, failure etc) so that
//    it will emit it in our fragment
    private val _signUpChannel = Channel<SignUpViewState>()
    val signUpChannel = _signUpChannel.receiveAsFlow()    //this will exposed in our fragment

    fun signUpYvAgent(signUpRequest : SignUpRequest) {

        viewModelScope.launch{
            _signUpChannel.send(SignUpViewState.Loading(R.string.sign_up))

            yvAgentSignupUseCase.invoke(signUpRequest).collect {
                when(it){
                    is Result.Success -> {
                        if (it.data is SignUpResponse){
                            _signUpChannel.send(SignUpViewState.Success(R.string.sign_up, it.data.data))
                        }
                    }
                    is Result.Failed -> {
                        _signUpChannel.send(SignUpViewState.Failure(R.string.sign_up, "Error from server"))
                    }
                    else -> {}
                }
            }
        }
    }


    fun getStates() = liveData {
        val stateList = getAllStatesUseCase.execute()
        val states: ArrayList<String> = arrayListOf()
        if (stateList != null){
            for (state in stateList){
                states.add(state.name)
            }
        }
        emit(states)
    }
}