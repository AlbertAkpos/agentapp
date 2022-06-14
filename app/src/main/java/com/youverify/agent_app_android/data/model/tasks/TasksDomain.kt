package com.youverify.agent_app_android.data.model.tasks

import com.google.gson.annotations.SerializedName

object TasksDomain {

    data class AgentTasksResponse(
        val success: Boolean,
        val taskItems: ArrayList<AgentTask>,
        val message: String? = null
    )

    data class AgentTask(
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
    }

    data class TaskAnswers(
        val buildingType: String = ""
    )

}