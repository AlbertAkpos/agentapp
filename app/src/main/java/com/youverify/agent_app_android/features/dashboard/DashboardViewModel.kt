package com.youverify.agent_app_android.features.dashboard

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
import com.youverify.agent_app_android.util.helper.formatDate
import com.youverify.agent_app_android.util.helper.getDateFromDayAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
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