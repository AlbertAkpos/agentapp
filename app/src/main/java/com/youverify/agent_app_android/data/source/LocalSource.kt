package com.youverify.agent_app_android.data.source

import androidx.lifecycle.LiveData
import com.youverify.agent_app_android.data.db.dao.TaskDao
import com.youverify.agent_app_android.data.model.entity.TaskEntity
import javax.inject.Inject

class LocalSource @Inject constructor (private val taskDao: TaskDao): ILocalSource {
    override suspend fun addTask(vararg taskItem: TaskEntity.TaskItem) {
        taskDao.addTask(*taskItem)
    }

    override suspend fun updateTask(vararg taskItem: TaskEntity.TaskItem) {
        taskDao.updateTask(*taskItem)
    }

    override suspend fun deleteTask(vararg taskItem: TaskEntity.TaskItem) {
        taskDao.deleteTask(*taskItem)
    }

    override fun fetchOfflineTasks(): LiveData<List<TaskEntity.TaskItem>> {
       return taskDao.getOfflineTasks()
    }

    override suspend fun deleteTask(taskId: String) {
        taskDao.deleteTask(taskId)
    }
}