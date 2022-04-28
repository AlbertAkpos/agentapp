package com.youverify.agent_app_android.viewmodel

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.model.TaskItem
import com.youverify.agent_app_android.view.activity.MainActivity

class TaskViewModel : ViewModel() {

    val verificationTypeText = MutableLiveData<String?>()      //data that will be in the verification type field
    val addressText = MutableLiveData<String?>()     //data that will be in the address field
    val timeText =  MutableLiveData<String?>()      //data that will be in the time field



//    fun getTaskList() = liveData {
//
//    }
}