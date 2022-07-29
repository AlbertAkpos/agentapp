package com.youverify.agent_app_android.data.model.tasks

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import com.youverify.agent_app_android.util.helper.getDateInMilliSecond
import com.youverify.agent_app_android.util.helper.getTimePassedSinceDate

object TasksDomain {

    data class AgentTasksResponse(
        val success: Boolean,
        val taskItems: ArrayList<AgentTask>,
        val message: String? = null
    )

    data class AgentTask(
        val id: String,
        val flatNumber: String,
        val displayVerificationType: String,
        val status: String?,
        val lga: String,
        val country: String,
        val buildingNumber: String,
        val landmark: String,
        val street: String,
        val city: String,
        val state: String,
        val businessName: String,
        val businessRegNumber: String,
        val candidate: Candidate?,
        val lastModifiedAt: String,
        val verificationType: String
    ) {
        val address: String get() = "$buildingNumber, $street, $city, $state, $country"
        val time get(): String {
           val timeMillisecond =  getDateInMilliSecond(lastModifiedAt) ?: return ""
           return getTimePassedSinceDate(timeMillisecond)
        }
    }

    data class Candidate(
        val lastName: String?,
        val lastModifiedAt: String?,
        val mobile: String?,
        val businessId: String?,
        val photo: String?,
        val firstName: String?,
        val createdAt: String?,
        val id: String?
    ) {
        val time get() = "$lastModifiedAt"
        val name get() = "$lastName $firstName"
    }

    data class TaskAnswers(
        val buildingType: String = "",
        val rejectionReason: String = "",
        val taskStarted: Boolean = false,
        val confirmedBy: String = "",
        val hasGate: Boolean = false,
        val buildingColor: String = "",
        val signatureLink: String = "",
        val needsConfirmation: Boolean? = null,
        val latLong: LatLong? = null
    )

    data class StartTaskResponse(
        val success: Boolean,
        val message: String
    )

    data class MessagesResponse(
        val success: Boolean?,
        val canLocationAddress: List<String>?,
        val cannotLocateAddress: List<String>,
        val canAccessBuilding: List<String>,
        val cannotAccessBuilding: List<String>
    )

    data class LatLong(
        val lat: Double,
        val long: Double
    )

    data class Color(
        @ColorRes val colorId: Int?,
        val name: String
    )

    data class GenericResponse(
        val success: Boolean,
        val statusCode: Int?,
        val message: String?,
    )

    data class SubmitTask(
        val taskId: String,
        var updateTaskRequest: TasksDto.UpdateTaskRequest,
        val message: String,
        val subitTaskRequest: TasksDto.SubmitTaskRequest,
        val offlinePhotos: List<String>? = null,
        val offlineSignature: String? = null
    )

    data class TasksStatusesResponse(
        val sucess: Boolean,
        val message: String?,
        val data: List<String>?
    )
}