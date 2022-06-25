package com.youverify.agent_app_android.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.youverify.agent_app_android.util.helper.getDateInMilliSecond
import com.youverify.agent_app_android.util.helper.getTimePassedSinceDate

object TaskEntity {
    @Entity(tableName = "task")
    data class TaskItem(
        @PrimaryKey
        @ColumnInfo(name = "taskId")
        val taskId: String,
        @ColumnInfo(name = "flatNumber")
        val flatNumber: String,
        @ColumnInfo(name = "verificationType")
        val verificationType: String,
        @ColumnInfo(name = "status")
        val status: String?,
        @ColumnInfo(name = "lga")
        val lga: String,
        @ColumnInfo(name = "country")
        val country: String,
        @ColumnInfo(name = "buildingNumber")
        val buildingNumber: String,
        @ColumnInfo(name = "landmark")
        val landmark: String,
        @ColumnInfo(name = "street")
        val street: String,
        @ColumnInfo(name = "city")
        val city: String,
        @ColumnInfo(name = "state")
        val state: String,
        @ColumnInfo(name = "businessName")
        val businessName: String,
        @ColumnInfo(name = "businessRegNumber")
        val businessRegNumber: String,
        @SerializedName("candidate")
        @Embedded
        val candidate: Candidate?,
        @ColumnInfo(name = "TaskItem.lastModifiedAt")
        val lastModifiedAt: String
    ) {
        val address: String get() = "$buildingNumber, $street, $city, $state, $country"
        val time get(): String {
            val timeMillisecond =  getDateInMilliSecond(lastModifiedAt) ?: return ""
            return getTimePassedSinceDate(timeMillisecond)
        }
    }

    data class Candidate(
        @ColumnInfo(name = "lastName")
        val lastName: String?,
        @ColumnInfo(name = "Candidate.lastModifiedAt")
        val lastModifiedAt: String?,
        @ColumnInfo(name = "mobile")
        val mobile: String?,
        @ColumnInfo(name = "businessId")
        val businessId: String?,
        @ColumnInfo(name = "photo")
        val photo: String?,
        @ColumnInfo(name = "firstName")
        val firstName: String?,
        @ColumnInfo(name = "createdAt")
        val createdAt: String?,
        @ColumnInfo(name = "id")
        val id: String?
    )
}