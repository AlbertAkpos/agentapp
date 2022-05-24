package com.youverify.agent_app_android.features.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.data.model.resetpassword.ResetPassResponse
import com.youverify.agent_app_android.data.model.response.ErrorMessage
import com.youverify.agent_app_android.domain.usecase.ResetPassUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPassViewModel @Inject constructor(
    private val resetPassUseCase: ResetPassUseCase)
    : ViewModel() {

    private val _resetPassChannel = Channel<ResetPassViewState>()
    val resetPassChannel = _resetPassChannel.receiveAsFlow()

    fun sendResetToken(email: Email){
        viewModelScope.launch {
            _resetPassChannel.send(ResetPassViewState.Loading(R.string.reset))

            resetPassUseCase.invoke(email).collect{
                when(it){
                    is Result.Success -> {
                        if (it.data is ResetPassResponse){
                            _resetPassChannel.send(ResetPassViewState.Success(R.string.reset, it.data))
                        }
                    }
                    is Result.Failed -> {
                        if(it.errorMessage is ErrorMessage){
                            _resetPassChannel.send(ResetPassViewState.Failure(R.string.reset, it.errorMessage.message))
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}