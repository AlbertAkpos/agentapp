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

}