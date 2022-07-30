package com.youverify.agent_app_android.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youverify.agent_app_android.data.db.dao.TaskDao
import com.youverify.agent_app_android.data.model.entity.TaskEntity

@Database(entities = [TaskEntity.TaskItem::class], version = 2, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AgentDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        private var instance: AgentDatabase? = null
        fun getDatabase(context: Context): AgentDatabase {
            return synchronized<AgentDatabase>(AgentDatabase::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AgentDatabase::class.java, "AgentDatabase_")
                        .fallbackToDestructiveMigration().build()
                }
                instance!!
            }
        }

    }
}