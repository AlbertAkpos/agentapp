package com.youverify.agent_app_android.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.util.helper.getDateInMilliSecond
import com.youverify.agent_app_android.util.helper.getTimePassedSinceDate

object TaskEntity {
    @Entity(tableName = "task")
    data class TaskItem(
        @PrimaryKey
        @ColumnInfo(name = "taskId")
        val taskId: String,
        @ColumnInfo(name = "agentTask")
        val agentTask: AgentTask,
        @ColumnInfo(name = "submitTask")
        val submitTask: SubmitTask? = null,
        @ColumnInfo(name = "agentId")
        val agentId: String
    )

    data class Candidate(
        @SerializedName("lastName")
        val lastName: String?,
        @SerializedName("Candidate.lastModifiedAt")
        val lastModifiedAt: String?,
        @SerializedName("mobile")
        val mobile: String?,
        @SerializedName("businessId")
        val businessId: String?,
        @SerializedName("photo")
        val photo: String?,
        @SerializedName("firstName")
        val firstName: String?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("id")
        val id: String?
    )

    data class UpdateTaskRequest(
        @SerializedName("gatePresent") val gatePresent: Boolean,
        @SerializedName("buildingColour") val buildingColour: String,
        @SerializedName("buildingType") val buildingType: String,
        @SerializedName("confirmedBy") val confirmedBy: String,
        @SerializedName("gateColour") val gateColor: String,
        @SerializedName("agentSignature") val agentSignature: String,
        @SerializedName("photos") val photos: List<UpdateTaskPhoto>,
        @SerializedName("location") val location: Coordinates
    )

    data class UpdateTaskPhoto(
        @SerializedName("url") val url: String?,
        @SerializedName("location") val location: Coordinates?
    )

    data class Coordinates(
        @SerializedName("lon") val long: Double?,
        @SerializedName("lat") val lat: Double?
    )

    data class AgentTask(
        val taskId: String,
        @SerializedName("flatNumber")
        val flatNumber: String,
        @SerializedName("verificationType")
        val verificationType: String,
        @SerializedName("status")
        val status: String?,
        @SerializedName("lga")
        val lga: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("buildingNumber")
        val buildingNumber: String,
        @SerializedName("landmark")
        val landmark: String,
        @SerializedName("street")
        val street: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("state")
        val state: String,
        @SerializedName("businessName")
        val businessName: String,
        @SerializedName("businessRegNumber")
        val businessRegNumber: String,
        @SerializedName("candidate")
        val candidate: Candidate?,
        @SerializedName("TaskItem.lastModifiedAt")
        val lastModifiedAt: String,
    ) {
        val address: String get() = "$buildingNumber, $street, $city, $state, $country"
        val time
            get(): String {
                val timeMillisecond = getDateInMilliSecond(lastModifiedAt) ?: return ""
                return getTimePassedSinceDate(timeMillisecond)
            }
    }

    data class SubmitTask(
        @SerializedName("taskId") val taskId: String,
        @SerializedName("UpdateTaskRequest") val updateTaskRequest: UpdateTaskRequest,
        @SerializedName("message") val message: String,
        @SerializedName("subitTaskRequest") val subitTaskRequest: SubmitTaskRequest
    )

    data class SubmitTaskRequest(
        @SerializedName("message") val message: String
    )


}