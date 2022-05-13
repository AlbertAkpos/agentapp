package com.youverify.agent_app_android.presentation.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {

    val verificationTypeText = MutableLiveData<String?>()      //data that will be in the verification type field
    val addressText = MutableLiveData<String?>()     //data that will be in the address field
    val timeText =  MutableLiveData<String?>()      //data that will be in the time field



//    fun getTaskList() = liveData {
//
//    }
}