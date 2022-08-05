package com.youverify.agent_app_android.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.youverify.agent_app_android.data.model.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(vararg task: TaskEntity.TaskItem)

    @Update
    suspend fun updateTask(vararg task: TaskEntity.TaskItem)

    @Delete
    suspend fun deleteTask(vararg task: TaskEntity.TaskItem)

    @Query("select * from task where agentId=:agentId")
    fun getOfflineTasks(agentId: String): LiveData<List<TaskEntity.TaskItem>>

    @Query("select * from task where agentId=:agentId")
    suspend fun fetchOfflineTasks(agentId: String): List<TaskEntity.TaskItem>

    @Query("delete from task where taskId =:taskId")
    suspend fun deleteTask(taskId: String)
}