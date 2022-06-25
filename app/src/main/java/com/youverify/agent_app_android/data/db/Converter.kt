package com.youverify.agent_app_android.data.db

import androidx.room.TypeConverter
import com.youverify.agent_app_android.data.model.entity.TaskEntity
import com.youverify.agent_app_android.util.extension.toJson
import com.youverify.agent_app_android.util.extension.toObject

class Converter {
    @TypeConverter
    fun toTaskRequest(json: String): TaskEntity.UpdateTaskRequest = json.toObject()

    @TypeConverter
    fun fromTaskRequest(taskRequest: TaskEntity.UpdateTaskRequest): String = taskRequest.toJson()

    @TypeConverter
    fun toTaskPhoto(json: String): TaskEntity.UpdateTaskPhoto = json.toObject()

    @TypeConverter
    fun fromTaskPhoto(photo: TaskEntity.UpdateTaskPhoto): String = photo.toJson()

    @TypeConverter
    fun toCoordinates(json: String): TaskEntity.Coordinates = json.toObject()

    @TypeConverter
    fun fromCoordinates(coordinates: TaskEntity.Coordinates) : String = coordinates.toJson()

    @TypeConverter
    fun toSubmitTask(json: String?): TaskEntity.SubmitTask? = json?.toObject()

    @TypeConverter
    fun fromSubmitTask(submitTask: TaskEntity.SubmitTask?): String? = submitTask?.toJson()

    @TypeConverter
    fun toAgentTask(json: String): TaskEntity.AgentTask = json.toObject()

    @TypeConverter
    fun fromAgentTask(agentTask: TaskEntity.AgentTask): String = agentTask.toJson()



}