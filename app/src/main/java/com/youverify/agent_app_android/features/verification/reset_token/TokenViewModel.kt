package com.youverify.agent_app_android.features.verification.reset_token

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.api.TokenInterceptor
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponse
import com.youverify.agent_app_android.data.model.response.ErrorMessage
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenResponse
import com.youverify.agent_app_android.domain.repository.LoginRepository
import com.youverify.agent_app_android.domain.usecase.LoginUseCase
import com.youverify.agent_app_android.domain.usecase.TokenUseCase
import com.youverify.agent_app_android.util.AgentSharePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenUseCase: TokenUseCase,
    private val interceptor: TokenInterceptor,
    private val loginRepository: LoginRepository,
    private val sharedPreference: AgentSharePreference
    ) : ViewModel() {

    private val _tokenChannel = Channel<TokenViewState>()
    val tokenChannel = _tokenChannel.receiveAsFlow()

    fun refreshToken(tokenRequest: TokenRequest) {
        viewModelScope.launch {
            _tokenChannel.send(TokenViewState.Loading(R.string.sign_in))

            tokenUseCase.invoke(tokenRequest).collect {

                when (it) {
                    is Result.Success -> {
                        if (it.data is TokenResponse) {
//                            interceptor.setToken(it.data.data.accessToken)
                            _tokenChannel.send(
                                TokenViewState.Success(
                                    R.string.sign_in,
                                    it.data
                                )
                            )
                        }
                    }
                    is Result.Failed -> {
                        if (it.errorMessage is ErrorMessage) {
                            _tokenChannel.send(
                                TokenViewState.Failure(
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

    fun sendFirebaseToken(token: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }
        Timber.d("token sent: ${sharedPreference.fcmTokenSent}")
        if (!sharedPreference.fcmTokenSent) {
            viewModelScope.launch (exceptionHandler){
                val response = loginRepository.submitFcmToken(Dto.FcmToken(token))
                sharedPreference.fcmTokenSent = response.success
            }
        }
    }
}