package com.youverify.agent_app_android.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.domain.repository.LoginRepository
import com.youverify.agent_app_android.util.AgentSharePreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseService: FirebaseMessagingService() {

    @Inject lateinit var loginRepository: LoginRepository

    @Inject lateinit var sharePreference: AgentSharePreference

    @Inject lateinit var scope: CoroutineScope


    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val loginToken = sharePreference.token
        if (loginToken.isNotEmpty()) {
            scope.launch(coroutineExceptionHandler) {
                loginRepository.submitFcmToken(Dto.FcmToken(token))
            }
        }
    }
}