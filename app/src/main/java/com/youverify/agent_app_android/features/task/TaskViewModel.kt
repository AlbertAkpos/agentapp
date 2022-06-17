package com.youverify.agent_app_android.features.task

import android.util.Log
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
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: ITaskRepository,
    private val supervisScope: CoroutineScope
) : ViewModel() {
    val taskItemState = MutableLiveData<SingleEvent<TasksDomain.AgentTask>>()

    val tasksState = MutableLiveData<ResultState<ArrayList<TasksDomain.AgentTask>>>()

    val canYouLocateTheAddressState = MutableLiveData<SingleEvent<Boolean>>()

    val typesOfBuildings = Constants.buildTypes

    val colors = Constants.colors

    var taskAnswers = TasksDomain.TaskAnswers()

    val imagesPicked = MutableLiveData<ArrayList<File>>()

    fun updateImagesPicked(file: File) {
        val values = imagesPicked.value ?: arrayListOf<File>()
        values.add(file)
        imagesPicked.postValue(values)
    }

    fun setTaskItem(taskItem: TasksDomain.AgentTask) {
        taskItemState.postValue(SingleEvent(taskItem))
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

            val startTaskResponse = async {
                repository.startTask(taskId)
            }

            val rejectionMessagesResponse = async {
                repository.getRejectionMessages()
            }

            val submissionMessagesResponse = async {
                repository.getSubmissionMessages()
            }

            kotlin.runCatching {
                val startTask = startTaskResponse.await()
                // Handle start task response
                if (startTask.success) {
                    startTaskState.postValue(SingleEvent(ResultState.Success(startTask.message)))
                    taskAnswers = taskAnswers.copy(taskStarted = true)
                } else {
                    startTaskState.postValue(SingleEvent(ResultState.Error(startTask.message)))
                }
            }

            kotlin.runCatching {
                // Handle rejectionMessages response
                val rejectionData = rejectionMessagesResponse.await()
                rejectionMessages.clear()
                Log.d("TaskViewModel", "Rejection messages ==> ${rejectionData.data}")
                rejectionMessages.addAll(rejectionData.data ?: emptyList())

            }



            kotlin.runCatching {
                // Handle submission reponse
                submissionMessages.clear()
                val submissionData = submissionMessagesResponse.await()
                submissionMessages.addAll(submissionData.data ?: emptyList())
            }


        }

    }

}