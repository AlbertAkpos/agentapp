package com.youverify.agent_app_android.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest
import com.youverify.agent_app_android.data.model.profile.ChangePassResponse
import com.youverify.agent_app_android.data.model.response.ErrorMessage
import com.youverify.agent_app_android.domain.usecase.ChangePassUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val changePassUseCase: ChangePassUseCase
    ) : ViewModel() {

    private val _changePassChannel = Channel<ChangePassViewState>()
    val changePassChannel = _changePassChannel.receiveAsFlow()

    fun changePassword(changePassRequest: ChangePassRequest) {
        viewModelScope.launch {
            _changePassChannel.send(ChangePassViewState.Loading(R.string.change_password))

            changePassUseCase.invoke(changePassRequest).collect {

                when (it) {
                    is Result.Success -> {
                        if (it.data is ChangePassResponse) {
                            _changePassChannel.send(
                                ChangePassViewState.Success(
                                    R.string.change_password,
                                    it.data
                                )
                            )
                        }
                    }
                    is Result.Failed -> {
                        if (it.errorMessage is ErrorMessage) {
                            _changePassChannel.send(
                                ChangePassViewState.Failure(
                                    R.string.change_password,
                                    it.errorMessage.message
                                )
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}