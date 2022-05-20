package com.youverify.agent_app_android.presentation.di

import com.youverify.agent_app_android.BuildConfig
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.api.AgentServiceFactory
import com.youverify.agent_app_android.data.api.TokenInterceptor
import com.youverify.agent_app_android.data.repository.signup.datasource.SignUpRemoteDataSource
import com.youverify.agent_app_android.data.repository.signup.datasourceImpl.SignUpRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteModule {

    @get:Binds
    val SignUpRemoteDataSourceImpl.signUpRemoteImpl: SignUpRemoteDataSource   //binding the remote to its impl

    companion object {
        @[Provides Singleton]
        fun provideApiService(tokenInterceptor: TokenInterceptor): AgentService =
            AgentServiceFactory.createApiService(BuildConfig.DEBUG, tokenInterceptor)
    }
}