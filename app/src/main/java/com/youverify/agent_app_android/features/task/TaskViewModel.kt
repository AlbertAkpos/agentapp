package com.youverify.agent_app_android.features.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.TaskItem
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import com.youverify.agent_app_android.util.Constants
import com.youverify.agent_app_android.util.SingleEvent
import com.youverify.agent_app_android.util.helper.ErrorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: ITaskRepository) : ViewModel() {
    val taskItemState = MutableLiveData<SingleEvent<TasksDomain.AgentTask>>()

    val tasksState = MutableLiveData<SingleEvent<ResultState<ArrayList<TasksDomain.AgentTask>>>>()

    val canYouLocateTheAddressState = MutableLiveData<SingleEvent<Boolean>>()

    val typesOfBuildings = Constants.buildTypes

    var taskAnswers = TasksDomain.TaskAnswers()

    fun setTaskItem(taskItem: TasksDomain.AgentTask) {
        taskItemState.postValue(SingleEvent(taskItem))
    }

    fun fetchAgentTasks(status: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            tasksState.postValue(SingleEvent(ResultState.Error(message)))
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            tasksState.postValue(SingleEvent(ResultState.Loading()))
            val response = repository.fetchAgentTasks()
            if (response.success) {
                tasksState.postValue(SingleEvent(ResultState.Success(response.taskItems)))
            } else {
                tasksState.postValue(SingleEvent(ResultState.Error(response.message ?: "Some error occurred")))
            }
        }
    }

    val startTaskState = MutableLiveData<SingleEvent<ResultState<String>>>()
    val rejectionMessages = MutableLiveData<List<String>?>()
    val submissionMessages = MutableLiveData<List<String>?>()

    /**
     * startTasks calls several endpoints
     */
    fun startTask(taskId: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            startTaskState.postValue(SingleEvent(ResultState.Error(message)))
        }

        val asynCoroutneExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            // Exception is handled silently
            throwable.printStackTrace()
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val startTaskResponse = async(coroutineExceptionHandler) {
                repository.startTask(taskId)
            }.await()

            val rejectionMessagesResponse = async(asynCoroutneExceptionHandler) {
                repository.getRejectionMessages()
            }.await()

            val submissionMessagesResponse = async(asynCoroutneExceptionHandler) {
                repository.getSubmissionMessages()
            }.await()

            // Handle start task response
            if (startTaskResponse.success) {
                startTaskState.postValue(SingleEvent(ResultState.Success(startTaskResponse.message)))
            } else {
                startTaskState.postValue(SingleEvent(ResultState.Error(startTaskResponse.message)))
            }

            // Handle rejectionMessages response
            rejectionMessages.postValue(rejectionMessagesResponse.data)

            // Handle submission reponse
            submissionMessages.postValue(submissionMessagesResponse.data)


        }
    }

}