package com.youverify.agent_app_android.core.functional



/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [Feature Failure] class.
 */

sealed class Failure {
    object NetworkConnection : Failure()
    object DataError : Failure()
    object ServerError : Failure()
}
