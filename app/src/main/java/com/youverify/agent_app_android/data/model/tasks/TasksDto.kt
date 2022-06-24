package com.youverify.agent_app_android.data.model.tasks

import com.google.gson.annotations.SerializedName

object TasksDto {
    data class AgentTasksResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode") val statusCode: Int?,
        @SerializedName("message") val message: String?,
        @SerializedName("data") val data: Data?,
        @SerializedName("links") val links: List<String>?
    )

    data class Data(
        @SerializedName("docs") val docs: List<Docs>?,
        @SerializedName("pagination") val pagination: Pagination?
    )

    data class Docs(
        @SerializedName("guarantor") val guarantor: Guarantor?,
        @SerializedName("business") val business: Business?,
        @SerializedName("address") val address: Address?,
        @SerializedName("status") val status: String?,
        @SerializedName("businessId") val businessId: String?,
        @SerializedName("createdBy") val createdBy: String?,
        @SerializedName("verificationType") val verificationType: String?,
        @SerializedName("_lastModifiedAt") val _lastModifiedAt: String?,
        @SerializedName("candidate") val candidate: Candidate?,
        @SerializedName("lastModifiedAt") val lastModifiedAt: String?,
        @SerializedName("_id") val _id: String?,
        @SerializedName("id") val id: String?
    )

    data class Pagination(
        @SerializedName("totalDocs") val totalDocs: Int?,
        @SerializedName("perPage") val perPage: Int?,
        @SerializedName("totalPages") val totalPages: Int?,
        @SerializedName("currentPage") val currentPage: Int?,
        @SerializedName("serialNo") val serialNo: Int?,
        @SerializedName("hasPrevPage") val hasPrevPage: Boolean?,
        @SerializedName("hasNextPage") val hasNextPage: Boolean?,
        @SerializedName("prevPage") val prevPage: String?,
        @SerializedName("nextPage") val nextPage: String?
    )

    data class Address(
        @SerializedName("flatNumber") val flatNumber: String?,
        @SerializedName("buildingName") val buildingName: String?,
        @SerializedName("subStreet") val subStreet: String?,
        @SerializedName("lga") val lga: String?,
        @SerializedName("country") val country: String?,
        @SerializedName("buildingNumber") val buildingNumber: Int?,
        @SerializedName("landmark") val landmark: String?,
        @SerializedName("street") val street: String?,
        @SerializedName("city") val city: String?,
        @SerializedName("state") val state: String?,
        @SerializedName("location") val location: Location,
    )

    data class Business(
        @SerializedName("name") val name: String?,
        @SerializedName("registrationNumber") val registrationNumber: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("mobile") val mobile: String?,
    )

    data class Guarantor(
        @SerializedName("firstName") val firstName: String?,
        @SerializedName("lastName") val lastName: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("mobile") val mobile: String?,
        @SerializedName("image") val image: String?
    )

    data class Location(
        @SerializedName("coordinates") val coordinates: Coordinates?,
        @SerializedName("type") val type: String?
    )

    data class Candidate(
        @SerializedName("lastName") val lastName: String?,
        @SerializedName("lastModifiedAt") val lastModifiedAt: String?,
        @SerializedName("mobile") val mobile: String?,
        @SerializedName("businessId") val businessId: String?,
        @SerializedName("photo") val photo: String?,
        @SerializedName("_lastModifiedAt") val _lastModifiedAt: String?,
        @SerializedName("firstName") val firstName: String?,
        @SerializedName("createdAt") val createdAt: String?,
        @SerializedName("createdBy") val createdBy: String?,
        @SerializedName("__v") val __v: Int?,
        @SerializedName("_createdAt") val _createdAt: String?,
        @SerializedName("_id") val _id: String?,
        @SerializedName("id") val id: String?
    )

    data class Coordinates(
        @SerializedName("lon") val long: Double?,
        @SerializedName("lat") val lat: Double?
    )

    data class StartTaskResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("status_code") val statusCode: Int?,
        @SerializedName("message") val message: String?
    )

    data class RejectionMessagesResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode") val statusCode: Int?,
        @SerializedName("message") val message: String?,
        @SerializedName("data") val data: List<String>?
    )

    data class SubmissionMessagesResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode") val statusCode: Int?,
        @SerializedName("message") val message: String?,
        @SerializedName("data") val data: List<String>?
    )

    data class SubmitTaskRequest(
        @SerializedName("message")
        val message: String
    )

    data class GenericResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode", alternate = ["status_code"]) val statusCode: Int?,
        @SerializedName("message") val message: String?,
    )

    data class Photos(
        @SerializedName("url") val url: String?,
        @SerializedName("location") val location: Location?
    )

    data class UpdateTaskRequest(
        @SerializedName("gatePresent") val gatePresent: Boolean,
        @SerializedName("buildingColour") val buildingColour: String,
        @SerializedName("buildingType") val buildingType: String,
        @SerializedName("confirmedBy") val confirmedBy: String,
        @SerializedName("agentSignature") val agentSignature: String,
        @SerializedName("photos") val photos: List<Photos>
    )

    data class TaskStatusesResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode", alternate = ["status_code"]) val statusCode: Int?,
        @SerializedName("message") val message: String?,
        @SerializedName("data") val data: List<String>?
    )
}