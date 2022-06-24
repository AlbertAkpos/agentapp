package com.youverify.agent_app_android.data.db.dao

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
}