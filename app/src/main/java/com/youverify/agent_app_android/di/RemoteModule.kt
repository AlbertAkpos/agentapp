package com.youverify.agent_app_android.di

import com.youverify.agent_app_android.BuildConfig
import com.youverify.agent_app_android.data.api.AgentService
import com.youverify.agent_app_android.data.api.AgentServiceFactory
import com.youverify.agent_app_android.data.api.TokenInterceptor
import com.youverify.agent_app_android.data.repository.login.datasource.LoginRemoteDataSource
import com.youverify.agent_app_android.data.repository.login.datasourceImpl.LoginRemoteDataSourceImpl
import com.youverify.agent_app_android.data.repository.resetpassword.datasource.ResetPassRemoteDataSource
import com.youverify.agent_app_android.data.repository.resetpassword.datasourceImpl.ResetPassDataSourceImpl
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
    val SignUpRemoteDataSourceImpl.signUpRemoteImpl: SignUpRemoteDataSource   //binding the sign-up remote to its impl

    @get:Binds
    val LoginRemoteDataSourceImpl.loginRemoteImpl: LoginRemoteDataSource   //binding the login remote to its impl

    @get:Binds
    val ResetPassDataSourceImpl.resetPassRemoteImpl: ResetPassRemoteDataSource  //binding the reset token remote to its impl

    companion object {
        @[Provides Singleton]
        fun provideApiService(tokenInterceptor: TokenInterceptor): AgentService =
            AgentServiceFactory.createApiService(BuildConfig.DEBUG, tokenInterceptor)
    }
}