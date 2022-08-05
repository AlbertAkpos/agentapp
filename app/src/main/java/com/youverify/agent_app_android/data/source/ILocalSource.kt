package com.youverify.agent_app_android.data.source

import androidx.lifecycle.LiveData
import com.youverify.agent_app_android.data.model.entity.TaskEntity

interface ILocalSource {
    suspend fun addTask(vararg taskItem: TaskEntity.TaskItem)
    suspend fun updateTask(vararg taskItem: TaskEntity.TaskItem)
    suspend fun deleteTask(vararg taskItem: TaskEntity.TaskItem)
    suspend fun deleteTask(taskId: String)
    fun fetchOfflineTasks(agentId: String): LiveData<List<TaskEntity.TaskItem>>
    suspend fun getOfflineNotifications(agentId: String): List<TaskEntity.TaskItem>
}