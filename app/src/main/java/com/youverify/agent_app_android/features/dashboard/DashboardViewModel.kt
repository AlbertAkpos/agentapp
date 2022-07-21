package com.youverify.agent_app_android.features.dashboard

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.common.Domain
import com.youverify.agent_app_android.data.model.common.Dto
import com.youverify.agent_app_android.domain.repository.IAgentRepository
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.SingleEvent
import com.youverify.agent_app_android.util.helper.ErrorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sharePreference: AgentSharePreference,
    private val agentRepository: IAgentRepository,
    private val taskRepository: ITaskRepository
) : ViewModel() {

    val agentVisibiltyStatus = MutableLiveData<String>()

    init {
        updateAgentVisibility(sharePreference.agentVisiblityStatus)
    }


    fun updateAgentVisibility(status: String) {
        agentVisibiltyStatus.postValue(status)
    }

    val updateAgentStatusState = MutableLiveData<SingleEvent<ResultState<String>>>()

    fun updateAgentStatus(status: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            updateAgentStatusState.postValue(SingleEvent(ResultState.Error(message)))
            //When update fails, update UI with current value
        }
        viewModelScope.launch(exceptionHandler) {
            val request = Dto.UpdateAgentStatusRequest(status)
            val response = agentRepository.updateAgentStatus(request)
            if (response.success && response.status != null) {
                //Update sharedpreference
                sharePreference.agentVisiblityStatus = response.status
                //Update views
                Timber.d("Status from remote ==> ${response.status}")
                updateAgentVisibility(response.status)
                updateAgentStatusState.postValue(
                    SingleEvent(
                        ResultState.Success(
                            response.message ?: "Status updated"
                        )
                    )
                )
            } else updateAgentStatusState.postValue(
                SingleEvent(
                    ResultState.Error(
                        response.message ?: "An error occurred"
                    )
                )
            )
        }
    }

    val analyticsDataState =
        MutableLiveData<ResultState<Domain.AgentTasksAnalyticsData>>()

    val previousPerformanceDataState = MutableLiveData<ResultState<Domain.AgentTasksAnalyticsData>>()

    val performanceCurrent = MutableLiveData<ResultState<Domain.AgentTasksAnalyticsData>>()

    val chartPercentage = MediatorLiveData<Int>().apply {
        fun update() {
            val previous = previousPerformanceDataState.value
            val current = performanceCurrent.value
            if (previous is ResultState.Success && current is ResultState.Success) {
                val previousCompleted = previous.data.completed
                val currentCompleted = current.data.completed
                if (previousCompleted != null && currentCompleted != null) {
                    val percentageChange =
                        kotlin.runCatching {
                            ((previousCompleted - currentCompleted) / previousCompleted) * 100
                        }.getOrDefault(0)
                    value = percentageChange
                }
            }
        }
        addSource(previousPerformanceDataState) {
            if (it is ResultState.Success) {
                update()
            }
        }

        addSource(performanceCurrent) {
            if (it is ResultState.Success) {
                update()
            }
        }
    }

    val todaysPerformance = MutableLiveData<ResultState<Domain.AgentTasksAnalyticsData>>()

    fun fetchCurrentPerformance(start: String, end: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            performanceCurrent.postValue(ResultState.Error(message))
        }

        viewModelScope.launch(exceptionHandler) {
            performanceCurrent.postValue(ResultState.Loading())
            val response = taskRepository.fetchAgentAnalyticsOnTasks(
                agentId = sharePreference.agentId, startDate = start, endDate = end)

            if (response.success && response.analyticsData != null) {
                performanceCurrent.postValue(ResultState.Success(response.analyticsData))
                if (todaysPerformance.value == null) {
                    todaysPerformance.postValue(ResultState.Success(response.analyticsData))
                }
            } else {
                performanceCurrent.postValue(ResultState.Error(response.message))
            }
        }
    }

    fun fetchPreviousPerfromance(start: String, end: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            previousPerformanceDataState.postValue(ResultState.Error(message))
        }

        viewModelScope.launch(exceptionHandler) {
            previousPerformanceDataState.postValue(ResultState.Loading())
            val response = taskRepository.fetchAgentAnalyticsOnTasks(
                agentId = sharePreference.agentId, startDate = start, endDate = end)

            if (response.success && response.analyticsData != null) {
                previousPerformanceDataState.postValue(ResultState.Success(response.analyticsData))
            } else {
                previousPerformanceDataState.postValue(ResultState.Error(response.message))
            }
        }
    }

    fun fetchAgentAnalytics(start: String, end: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            analyticsDataState.postValue(ResultState.Error(message))
        }

        viewModelScope.launch(exceptionHandler) {
            analyticsDataState.postValue(ResultState.Loading())
            val response = taskRepository.fetchAgentAnalyticsOnTasks(
                agentId = sharePreference.agentId, startDate = start, endDate = end)

            if (response.success && response.analyticsData != null) {
                analyticsDataState.postValue(ResultState.Success(response.analyticsData))
            } else {
                analyticsDataState.postValue(ResultState.Error(response.message))
            }
        }
    }


}