package com.youverify.agent_app_android.data.model.profile

data class ChangePassRequest (
    val oldPassword: String,
    val newPassword: String)