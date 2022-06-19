package com.youverify.agent_app_android.features.verification.id

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.data.model.verification.id.VerifyIdResponse
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import com.youverify.agent_app_android.domain.usecase.UploadUseCase
import com.youverify.agent_app_android.domain.usecase.VerifyIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val uploadUseCase: UploadUseCase,
    private val verifyIdUseCase: VerifyIdUseCase
) : ViewModel() {

    private val _uploadChannel = Channel<UploadViewState>()
    private val _verifyIdChannel = Channel<VerifyIdViewState>()
    val uploadChannel = _uploadChannel.receiveAsFlow()
    val verifyIdChannel = _verifyIdChannel.receiveAsFlow()

    //function for uploading the user photo
    fun uploadImage(uploadRequest: MultipartBody.Part?) {
        viewModelScope.launch {
            _uploadChannel.send(UploadViewState.Loading(R.string.upload))

            uploadUseCase.invoke(uploadRequest).collect {

                when (it) {
                    is Result.Success -> {
                        if (it.data is UploadImageResponse) {
                            _uploadChannel.send(UploadViewState.Success(R.string.upload, it.data))
                        }
                    }
                    is Result.Failed -> {
                        if (it.errorMessage is String) {
                            _uploadChannel.send(
                                UploadViewState.Failure(
                                    R.string.upload,
                                    it.errorMessage
                                )
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }


    //function to send the user id details for verification
    fun verifyId(verifyIDRequest: VerifyIDRequest) {
        viewModelScope.launch {
            _verifyIdChannel.send(VerifyIdViewState.Loading(R.string.verify))

            verifyIdUseCase.invoke(verifyIDRequest).collect {
                when (it) {
                    is Result.Success -> {
                        if (it.data is VerifyIdResponse) {
                            _verifyIdChannel.send(
                                VerifyIdViewState.Success(
                                    R.string.verify,
                                    it.data
                                )
                            )
                        }
                    }
                    is Result.Failed -> {
                        _verifyIdChannel.send(
                            VerifyIdViewState.Failure(
                                R.string.verify,
                                it.errorMessage.toString()
                            )
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}