package com.youverify.agent_app_android.features.verification.id

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.data.model.upload.UploadResponse
import com.youverify.agent_app_android.domain.usecase.UploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val uploadUseCase: UploadUseCase
) : ViewModel() {

    private val _uploadChannel = Channel<UploadViewState>()
    val uploadChannel = _uploadChannel.receiveAsFlow()

    fun uploadImage(uploadRequest: MultipartBody.Part?) {
        viewModelScope.launch {
            _uploadChannel.send(UploadViewState.Loading(R.string.upload))

            uploadUseCase.invoke(uploadRequest).collect {

                when (it) {
                    is Result.Success -> {
                        if (it.data is UploadResponse) {
                            _uploadChannel.send(UploadViewState.Success(R.string.upload, it.data))
                        }
                    }
                    is Result.Failed -> {
                        _uploadChannel.send(
                            UploadViewState.Failure(
                                R.string.upload,
                                "Unable to upload Image"
                            )
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}