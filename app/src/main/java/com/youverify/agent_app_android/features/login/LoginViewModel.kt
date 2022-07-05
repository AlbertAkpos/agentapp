package com.youverify.agent_app_android.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.TokenInterceptor
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.response.ErrorMessage
import com.youverify.agent_app_android.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val interceptor: TokenInterceptor
) : ViewModel() {

    private val _loginChannel = Channel<LoginViewState>()
    val loginChannel = _loginChannel.receiveAsFlow()

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginChannel.send(LoginViewState.Loading(R.string.sign_in))

            loginUseCase.invoke(loginRequest).collect {

                when (it) {
                    is Result.Success -> {
                        if (it.data is LoginResponse) {
                            interceptor.setToken(it.data.data.token)
                            _loginChannel.send(
                                LoginViewState.Success(
                                    R.string.sign_in,
                                    it.data.data
                                )
                            )
                        }
                    }
                    is Result.Failed -> {
                        if (it.errorMessage is ErrorMessage) {
                            _loginChannel.send(
                                LoginViewState.Failure(
                                    R.string.sign_in,
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