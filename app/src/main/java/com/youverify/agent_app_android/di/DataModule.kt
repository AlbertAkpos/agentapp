package com.youverify.agent_app_android.di

import com.youverify.agent_app_android.data.repository.*
import com.youverify.agent_app_android.data.repository.tasks.TasksRepository
import com.youverify.agent_app_android.data.repository.verification.refresh_token.TokenDataSource
import com.youverify.agent_app_android.data.repository.verification.refresh_token.TokenDataSourceImpl
import com.youverify.agent_app_android.data.source.AgentDataSource
import com.youverify.agent_app_android.data.source.IAgentSource
import com.youverify.agent_app_android.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @get:Binds
    val SignUpRepositoryImpl.signUpRepoImpl: SignUpRepository   //binding the  sign up repo to its impl

    @get:Binds
    val LoginRepositoryImpl.loginRepoImpl: LoginRepository   //binding the  login repo to its impl

    @get:Binds
    val ResetPassRepositoryImpl.resetPassRepoImpl: ResetPassRepository   //binding the  reset token repo to its impl

    @get:Binds
    val UploadRepositoryImpl.uploadRepoImpl: UploadRepository

    @get:Binds
    val StateRepositoryImpl.stateRepoImpl: StateRepository

    @get:Binds
    val StateLgaRepositoryImpl.stateLgaRepoImpl: StateLgaRepository

    @get:Binds
    val PrefAreasRepositoryImpl.prefAreasRepoImpl: PrefAreasRepository

    @get:Binds
    val VerifyIdRepositoryImpl.verifyIdRepoImpl: VerifyIdRepository

    @get:Binds
    val TasksRepository.taskRepository: ITaskRepository

    @get:Binds
    val AgentDataSource.agentDataSource: IAgentSource

    @get:Binds
    val TokenRepositoryImpl.tokenRepository: TokenRepository

    @get:Binds
    val ChangePassRepositoryImpl.changePassRepository: ChangePassRepository
}