package com.youverify.agent_app_android.di

import com.youverify.agent_app_android.util.ProgressLoader
import com.youverify.agent_app_android.util.ProgressLoaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface UIModule {

    @get:Binds
    val ProgressLoaderImpl.progressLoader: ProgressLoader
}