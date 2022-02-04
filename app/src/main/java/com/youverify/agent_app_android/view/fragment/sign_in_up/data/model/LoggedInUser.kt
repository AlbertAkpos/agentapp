package com.youverify.agent_app_android.view.fragment.sign_in_up.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String
)