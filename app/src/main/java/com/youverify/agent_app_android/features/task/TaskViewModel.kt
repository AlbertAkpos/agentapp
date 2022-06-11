package com.youverify.agent_app_android.features.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.youverify.agent_app_android.data.model.TaskItem
import com.youverify.agent_app_android.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class TaskViewModel : ViewModel() {
    val taskItemState = MutableLiveData<SingleEvent<TaskItem>>()

    fun setTaskItem(taskItem: TaskItem) {
        taskItemState.postValue(SingleEvent(taskItem))
    }
}