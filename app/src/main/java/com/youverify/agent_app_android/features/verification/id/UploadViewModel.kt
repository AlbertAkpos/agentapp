package com.youverify.agent_app_android.features.verification.id

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.Result
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.response.ErrorMessage
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.data.model.verification.id.VerifyIdResponse
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import com.youverify.agent_app_android.data.repository.tasks.TasksRepository
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import com.youverify.agent_app_android.domain.usecase.UploadUseCase
import com.youverify.agent_app_android.domain.usecase.VerifyIdUseCase
import com.youverify.agent_app_android.util.SingleEvent
import com.youverify.agent_app_android.util.helper.ErrorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val uploadUseCase: UploadUseCase,
    private val verifyIdUseCase: VerifyIdUseCase,
    private val tasksRepository: ITaskRepository
) : ViewModel() {

    private val _uploadChannel = Channel<UploadViewState>()
    private val _verifyIdChannel = Channel<VerifyIdViewState>()
    val uploadChannel = _uploadChannel.receiveAsFlow()
    val verifyIdChannel = _verifyIdChannel.receiveAsFlow()

    val uploadState = uploadChannel.mapLatest {
        SingleEvent(it)
    }.asLiveData()

    val imagesUploadState = MutableLiveData<SingleEvent<ResultState<Pair<List<String>?, Int>>>>()

    //function for uploading the user photo
    fun uploadImage(
        uploadRequest: MultipartBody.Part?,
        file: File? = null,
        imageType: Int = UploadViewState.Companion.UploadType.imageUpload
    ) {

        viewModelScope.launch {
            _uploadChannel.send(UploadViewState.Loading(R.string.upload))

            uploadUseCase.invoke(uploadRequest).collect {

                when (it) {
                    is Result.Success -> {
                        if (it.data is UploadImageResponse) {
                            _uploadChannel.send(
                                UploadViewState.Success(
                                    R.string.upload,
                                    it.data,
                                    file,
                                    imageType
                                )
                            )
                        }
                    }
                    is Result.Failed -> {
                        if (it.errorMessage is String) {
                            _uploadChannel.send(
                                UploadViewState.Failure(
                                    R.string.upload,
                                    it.errorMessage,
                                    file,
                                    imageType
                                )
                            )
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun uploadImages(uploadRequests: List<MultipartBody.Part>, imageType: Int = UploadViewState.Companion.UploadType.imageUpload, callback: (urls: List<String>) -> Unit) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            imagesUploadState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(exceptionHandler) {
            imagesUploadState.postValue(SingleEvent(ResultState.Loading()))
           val response = tasksRepository.uploadImages(uploadRequests)
            if (response.success) {
                imagesUploadState.postValue(SingleEvent(ResultState.Success(Pair(response.urls, imageType))))
                callback(response.urls ?: emptyList())
            } else {
                imagesUploadState.postValue(SingleEvent(ResultState.Error(response.message ?: "An error occurred")))
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
                        if (it.errorMessage is ErrorMessage) {
                            _verifyIdChannel.send(
                                VerifyIdViewState.Failure(
                                    R.string.verify,
                                    it.errorMessage.message
                                )
                            )
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}