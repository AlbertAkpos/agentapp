package com.youverify.agent_app_android.features.task

import androidx.lifecycle.*
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.domain.repository.ITaskRepository
import com.youverify.agent_app_android.domain.usecase.GetStatesUseCase
import com.youverify.agent_app_android.util.Constants
import com.youverify.agent_app_android.util.SingleEvent
import com.youverify.agent_app_android.util.TaskStatus
import com.youverify.agent_app_android.util.helper.ErrorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: ITaskRepository,
    private val supervisScope: CoroutineScope,
    private val getAllStatesUseCase: GetStatesUseCase
) : ViewModel() {
    val taskItemState = MutableLiveData<SingleEvent<TasksDomain.AgentTask>>()

    var currentTask: TasksDomain.AgentTask? = null
        private set

    private val agentId by lazy { repository.fetchAgentId() }

    val tasksState = MutableLiveData<SingleEvent<ResultState<ArrayList<TasksDomain.AgentTask>>>>()

    val canYouLocateTheAddressState = MutableLiveData<SingleEvent<Boolean>>()

    val doesBuildingHaveGate = MutableLiveData<SingleEvent<Boolean>>()

    val typesOfBuildings = Constants.buildTypes

    val colors = Constants.colors

    var taskAnswers = TasksDomain.TaskAnswers()

    val candidateAddressConfirmedBy get() = if (taskAnswers.needsConfirmation == true) whoConfirmedAddressNegative else whoConfirmedAddressPositive

    private val whoConfirmedAddressPositive = arrayListOf<String>()

    private val whoConfirmedAddressNegative = arrayListOf<String>()

    val imagesPicked = MutableLiveData<ArrayList<File>>()

    val doesCandidateLiveAtAddress = MutableLiveData<SingleEvent<Boolean>>()

    val uploadedImages = arrayListOf<String>()

    val canAccessBuildingState = MutableLiveData<SingleEvent<Boolean>>()

    val notifications = MediatorLiveData<ArrayList<NotificationItem>>().apply {
        addSource(repository.fetchOfflineNotifications(agentId)) { updateNotifications(it.filter { notification -> notification.submitTask != null }) }
    }

    val offlineTasks =
        Transformations.map(repository.fetchOfflineTasks(agentId)) { it.filter { item -> !item.taskUpdated } }

     fun updateNotifications(offlineTasks: List<NotificationItem>) {
        val currentNotifications = arrayListOf<NotificationItem>()
        currentNotifications.addAll(offlineTasks)
        notifications.postValue(currentNotifications)
    }

    fun refresh() {
        viewModelScope.launch {
            val items = repository.getOfflineNotifications(agentId)
            updateNotifications(items)
        }
    }


    fun updateImagesPicked(file: File) {
        val values = imagesPicked.value ?: arrayListOf<File>()
        values.add(file)
        imagesPicked.postValue(values)
    }

    fun setTaskItem(taskItem: TasksDomain.AgentTask) {
        taskItemState.postValue(SingleEvent(taskItem))
        currentTask = taskItem
    }

    fun fetchAgentTasks(state: String? = null, status: String? = null) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            tasksState.postValue(SingleEvent(ResultState.Error(message)))
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            tasksState.postValue(SingleEvent(ResultState.Loading()))
            val response = repository.fetchAgentTasks(state, status)
            if (response.success) {
                tasksState.postValue(SingleEvent(ResultState.Success(response.taskItems)))
            } else {
                tasksState.postValue(
                    SingleEvent(
                        ResultState.Error(
                            response.message ?: "Some error occurred"
                        )
                    )
                )
            }
        }
    }

    val startTaskState = MutableLiveData<SingleEvent<ResultState<TasksDomain.MessagesResponse>>>()
    val rejectionMessages = arrayListOf<String>()
    val submissionMessages = arrayListOf<String>()

    val offlinePhotos = arrayListOf<String>()
    var offlineSignature = ""

    val cantLocateAddressReasons = Constants.cantLocateAddressReasons

    /**
     * startTasks calls several endpoints
     */
    fun startTask(taskId: String, verificationType: String, status: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            startTaskState.postValue(SingleEvent(ResultState.Error(message)))
        }


        supervisScope.launch(coroutineExceptionHandler) {
            startTaskState.postValue(SingleEvent(ResultState.Loading("Starting task...")))


            val submissionMessagesResponse = async {
                repository.getSubmissionMessages(verificationType)
            }

            // Handle submission reponse
            submissionMessages.clear()
            val submissionData = submissionMessagesResponse.await()
            submissionMessages.addAll(submissionData.canLocationAddress ?: emptyList())

            cantLocateAddressReasons.clear()
            cantLocateAddressReasons.addAll(submissionData.cannotLocateAddress)

            whoConfirmedAddressNegative.clear()
            whoConfirmedAddressNegative.addAll(submissionData.candidateDoesNotLiveThere)

            whoConfirmedAddressPositive.clear()
            whoConfirmedAddressPositive.addAll(submissionData.candidateLivesThere)

            if (status == TaskStatus.unasigned) {
                val startTaskResponse = async {
                    repository.startTask(taskId)
                }
                val startTask = startTaskResponse.await()
                // Handle start task response
                if (startTask.success) {
                    startTaskState.postValue(SingleEvent(ResultState.Success(submissionData)))
                    taskAnswers = taskAnswers.copy(taskStarted = true)
                    //Put the task in local db
                    currentTask = currentTask?.copy(status = TaskStatus.started)
                    currentTask?.let { repository.addTask(currentTask!!, agentId) }
                } else {
                    startTaskState.postValue(SingleEvent(ResultState.Error(startTask.message)))
                }

                return@launch

            }


            startTaskState.postValue(SingleEvent(ResultState.Success(submissionData)))


        }

    }

    val taskRejectionState = MutableLiveData<SingleEvent<ResultState<String>>>()

    val updateAndSubmitTaskState = MutableLiveData<SingleEvent<ResultState<String>>>()

    fun rejectTask(request: TasksDto.SubmitTaskRequest, taskId: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            taskRejectionState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            taskRejectionState.postValue(SingleEvent(ResultState.Loading()))
            val response = repository.submitTask(request, taskId)
            if (response.success) {
                taskRejectionState.postValue(
                    SingleEvent(
                        ResultState.Success(
                            response.message ?: "Task submitted successfully"
                        )
                    )
                )
            } else {
                taskRejectionState.postValue(
                    SingleEvent(
                        ResultState.Error(
                            response.message ?: "An error occurred"
                        )
                    )
                )
            }
        }
    }

    fun updateAndSubmitTask(
        taskItem: TasksDomain.SubmitTask,
        notificationItem: NotificationItem? = null
    ) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            updateAndSubmitTaskState.postValue(SingleEvent(ResultState.Error(message)))
//            if (notificationItem != null) {
//                // Put the notificatio Item back if submission fails. This is just to update the UI
//                updateNotifications(listOf(notificationItem))
//            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            updateAndSubmitTaskState.postValue(SingleEvent(ResultState.Loading()))
            // Update the task on local
            currentTask?.let { repository.addTask(currentTask!!, agentId) }
            currentTask?.let { repository.updateTask(taskItem, currentTask!!, agentId) }
            // Update on remote
            val response =
                repository.updateTask(taskId = taskItem.taskId, taskItem.updateTaskRequest)
            if (response.success) {

                val taskSubmissionResponse = repository.submitTask(
                    request = taskItem.subitTaskRequest,
                    taskId = taskItem.taskId
                )
                if (response.success) {
                    updateAndSubmitTaskState.postValue(
                        SingleEvent(
                            ResultState.Success(
                                taskSubmissionResponse.message ?: "Task submitted successfully"
                            )
                        )
                    )
                    repository.deleteTask(taskItem.taskId)
                } else updateAndSubmitTaskState.postValue(
                    SingleEvent(
                        ResultState.Error(
                            taskSubmissionResponse.message ?: "An error occurred. Please try again"
                        )
                    )
                )

            } else updateAndSubmitTaskState.postValue(
                SingleEvent(
                    ResultState.Error(
                        response.message ?: "An error occurred. Please try again"
                    )
                )
            )

        }
    }

    fun updateTaskOnLocale(taskItem: TasksDomain.SubmitTask) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }
        viewModelScope.launch(exceptionHandler) {
            Timber.d("Cuurent task: ==> $currentTask")
            currentTask?.let { repository.updateTask(taskItem, it, agentId) }
        }
    }

    val submitTaskState = MutableLiveData<SingleEvent<ResultState<String>>>()

    fun submitTask(submitRequest: TasksDto.SubmitTaskRequest, taskId: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            submitTaskState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            submitTaskState.postValue(SingleEvent(ResultState.Loading()))
            val response = repository.submitTask(submitRequest, taskId)
            if (response.success) {
                submitTaskState.postValue(
                    SingleEvent(
                        ResultState.Success(
                            response.message ?: "Successfully notified the business"
                        )
                    )
                )
            } else submitTaskState.postValue(
                SingleEvent(
                    ResultState.Error(
                        response.message ?: "An error occurred"
                    )
                )
            )
        }

    }

    val filterParamsState =
        MutableLiveData<SingleEvent<ResultState<Pair<List<State>, TasksDomain.TasksStatusesResponse>>>>()


    fun getFilterParams() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            val messsage = ErrorHelper.handleException(throwable)
            filterParamsState.postValue(SingleEvent(ResultState.Error(messsage)))
        }
        viewModelScope.launch(exceptionHandler) {
            filterParamsState.postValue(SingleEvent(ResultState.Loading()))
            val stateDeferred = async {
                getAllStatesUseCase.execute() ?: emptyList()
            }

            val statusesDefferred = async {
                repository.getTaskStatuses()
            }
            val states = stateDeferred.await()
            val statuses = statusesDefferred.await()

            if (statuses.data != null) {
                filterParamsState.postValue(
                    SingleEvent(
                        ResultState.Success(
                            Pair(
                                states,
                                statuses
                            )
                        )
                    )
                )
            } else filterParamsState.postValue(
                SingleEvent(
                    ResultState.Error(
                        statuses.message ?: "An error occurred. Please try again"
                    )
                )
            )

        }
    }

}