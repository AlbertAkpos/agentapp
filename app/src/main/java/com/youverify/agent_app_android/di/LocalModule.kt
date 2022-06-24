package com.youverify.agent_app_android.di

import android.content.Context
import com.youverify.agent_app_android.data.db.AgentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    fun provideAgentDatabase(@ApplicationContext context: Context): AgentDatabase = AgentDatabase.getDatabase(context)

    @Provides
    fun provideTaskDao(database: AgentDatabase) = database.taskDao()
}