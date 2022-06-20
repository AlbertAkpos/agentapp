package com.youverify.agent_app_android.data.model.tasks

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName

object TasksDomain {

    data class AgentTasksResponse(
        val success: Boolean,
        val taskItems: ArrayList<AgentTask>,
        val message: String? = null
    )

    data class AgentTask(
        val id: String,
        val flatNumber: String,
        val verificationType: String,
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
        val candidate: Candidate?
    ) {
        val address: String get() = "$flatNumber, $street, $city, $state, $country"
        val time get() = "$"
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
        val hasGate: Boolean = false
    )

    data class StartTaskResponse(
        val success: Boolean,
        val message: String
    )

    data class MessagesResponse(
        val success: Boolean?,
        val data: List<String>?
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
        val task: TasksDto.UpdateTaskRequest,
        val message: String,
        val subitTaskRequest: TasksDto.SubmitTaskRequest
    )

}