package com.youverify.agent_app_android.di

import com.youverify.agent_app_android.data.repository.LoginRepositoryImpl
import com.youverify.agent_app_android.data.repository.ResetPassRepositoryImpl
import com.youverify.agent_app_android.data.repository.SignUpRepositoryImpl
import com.youverify.agent_app_android.domain.repository.LoginRepository
import com.youverify.agent_app_android.domain.repository.ResetPassRepository
import com.youverify.agent_app_android.domain.repository.SignUpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @get:Binds
    val SignUpRepositoryImpl.signUpRepoImpl: SignUpRepository   //binding the  sign up repo to its impl

    @get:Binds
    val LoginRepositoryImpl.loginRepoImpl: LoginRepository   //binding the  login repo to its impl

    @get:Binds
    val ResetPassRepositoryImpl.resetPassRepoImpl: ResetPassRepository   //binding the  reset token repo to its impl\
}