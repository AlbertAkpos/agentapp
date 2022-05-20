package com.youverify.agent_app_android.data.model.signup


data class SignUpRequest (
    val firstName : String,
    val lastName : String,
    val phoneNumber : String,
    val emailAddress : String,
    val stateOfResidence : String,
    val fieldPartnerId : String,
    val password : String
)

