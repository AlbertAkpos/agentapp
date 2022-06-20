package com.youverify.agent_app_android.features.task

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import com.youverify.agent_app_android.util.Constants
import com.youverify.agent_app_android.util.SingleEvent
import com.youverify.agent_app_android.util.helper.ErrorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: ITaskRepository,
    private val supervisScope: CoroutineScope
) : ViewModel() {
    val taskItemState = MutableLiveData<SingleEvent<TasksDomain.AgentTask>>()

    var currentTask: TasksDomain.AgentTask ? = null
        private set

    val tasksState = MutableLiveData<ResultState<ArrayList<TasksDomain.AgentTask>>>()

    val canYouLocateTheAddressState = MutableLiveData<SingleEvent<Boolean>>()

    val doesBuildingHaveGate = MutableLiveData<SingleEvent<Boolean>>()

    val typesOfBuildings = Constants.buildTypes

    val colors = Constants.colors

    val candidateAddressConfirmedBy = Constants.whoConfirmedCandidateAddressList

    var taskAnswers = TasksDomain.TaskAnswers()

    val imagesPicked = MutableLiveData<ArrayList<File>>()

    val doesCandidateLiveAtAddress = MutableLiveData<SingleEvent<Boolean>>()

    val uploadedImages = arrayListOf<String>()


    fun updateImagesPicked(file: File) {
        val values = imagesPicked.value ?: arrayListOf<File>()
        values.add(file)
        imagesPicked.postValue(values)
    }

    fun setTaskItem(taskItem: TasksDomain.AgentTask) {
        taskItemState.postValue(SingleEvent(taskItem))
        currentTask = taskItem
    }

    fun fetchAgentTasks(status: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            tasksState.postValue(ResultState.Error(message))
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            tasksState.postValue(ResultState.Loading())
            val response = repository.fetchAgentTasks()
            if (response.success) {
                tasksState.postValue(ResultState.Success(response.taskItems))
            } else {
                tasksState.postValue(ResultState.Error(response.message ?: "Some error occurred"))
            }
        }
    }

    val startTaskState = MutableLiveData<SingleEvent<ResultState<String>>>()
    val rejectionMessages = arrayListOf<String>()
    val submissionMessages = arrayListOf<String>()

    /**
     * startTasks calls several endpoints
     */
    fun startTask(taskId: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            startTaskState.postValue(SingleEvent(ResultState.Error(message)))
        }


        supervisScope.launch(coroutineExceptionHandler) {
            startTaskState.postValue(SingleEvent(ResultState.Loading("Starting task...")))
            val startTaskResponse = async {
                repository.startTask(taskId)
            }

            val rejectionMessagesResponse = async {
                repository.getRejectionMessages()
            }

            val submissionMessagesResponse = async {
                repository.getSubmissionMessages()
            }


            val startTask = startTaskResponse.await()
            // Handle start task response
            if (startTask.success) {
                startTaskState.postValue(SingleEvent(ResultState.Success(startTask.message)))
                taskAnswers = taskAnswers.copy(taskStarted = true)
            } else {
                startTaskState.postValue(SingleEvent(ResultState.Error(startTask.message)))
            }

            // Handle rejectionMessages response
            val rejectionData = rejectionMessagesResponse.await()
            rejectionMessages.clear()
            Log.d("TaskViewModel", "Rejection messages ==> ${rejectionData.data}")
            rejectionMessages.addAll(rejectionData.data ?: emptyList())

            // Handle submission reponse
            submissionMessages.clear()
            val submissionData = submissionMessagesResponse.await()
            submissionMessages.addAll(submissionData.data ?: emptyList())



        }

    }

    val taskRejectionState = MutableLiveData<SingleEvent<ResultState<String>>>()

    val taskSubmissionState = MutableLiveData<SingleEvent<ResultState<String>>>()

    fun rejectTask(request: TasksDto.SubmitTaskRequest, taskId: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            taskRejectionState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            taskRejectionState.postValue(SingleEvent(ResultState.Loading()))
            val response = repository.submitTask(request, taskId)
            if (response.success) {
                taskRejectionState.postValue(SingleEvent(ResultState.Success(response.message ?: "Task submitted successfully")))
            } else {
                taskRejectionState.postValue(SingleEvent(ResultState.Error(response.message ?: "An error occurred")))
            }
        }
    }

    fun submitTask(taskItem: TasksDomain.SubmitTask) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            taskSubmissionState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(coroutineExceptionHandler) {

                val response = repository.updateTask(taskId = taskItem.taskId, taskItem.task)
                if (response.success) {

                    val taskSubmissionResponse = repository.submitTask(request = taskItem.subitTaskRequest, taskId = taskItem.taskId)
                    if (response.success) {
                        taskSubmissionState.postValue(SingleEvent(ResultState.Success(taskSubmissionResponse.message ?: "Task submitted successfully")))
                    } else taskSubmissionState.postValue(SingleEvent(ResultState.Error(taskSubmissionResponse.message ?: "An error occurred. Please try again")))

                } else taskSubmissionState.postValue(SingleEvent(ResultState.Error(response.message ?: "An error occurred. Please try again")))

        }
    }

}