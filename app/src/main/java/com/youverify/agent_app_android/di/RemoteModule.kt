package com.youverify.agent_app_android.di

import com.youverify.agent_app_android.BuildConfig
import com.youverify.agent_app_android.data.api.*
import com.youverify.agent_app_android.data.repository.login.LoginRemoteDataSource
import com.youverify.agent_app_android.data.repository.login.LoginRemoteDataSourceImpl
import com.youverify.agent_app_android.data.repository.resetpassword.ResetPassRemoteDataSource
import com.youverify.agent_app_android.data.repository.resetpassword.ResetPassDataSourceImpl
import com.youverify.agent_app_android.data.repository.signup.SignUpRemoteDataSource
import com.youverify.agent_app_android.data.repository.signup.SignUpRemoteDataSourceImpl
import com.youverify.agent_app_android.data.repository.upload.UploadRemoteDataSource
import com.youverify.agent_app_android.data.repository.upload.UploadRemoteDataSourceImpl
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

    @get:Binds
    val UploadRemoteDataSourceImpl.uploadRemoteImpl: UploadRemoteDataSource

    companion object {
        @[Provides Singleton]
        fun provideApiService(tokenInterceptor: TokenInterceptor): AgentService =
            AgentServiceFactory.createApiService(BuildConfig.DEBUG, tokenInterceptor)

        @[Provides Singleton]
        fun provideUploadApiService(tokenInterceptor: TokenInterceptor): UploadService =
            UploadServiceFactory.createApiService(BuildConfig.DEBUG, tokenInterceptor)
    }

}