package com.youverify.agent_app_android.util.helper

import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object ErrorHelper {
    fun handleException(exception: Exception): String {
        val message =  when (exception) {
            is TimeoutException -> "Connection timeout. Please try again"
            is ConnectException -> "Couldn't connect. Please check your internet"
            is SocketTimeoutException -> "Connection timeout. Please check your internet connection"
            is UnknownHostException -> "Couldn't connect to server. Please check your internet connection"
            is HttpException ->  "Server returned an error ( Code: ${exception.code()} ). Please try again"

            else -> "An error occurred. Please try again"
        }

        return if (message.length > 100) "Server returned an error. Please try again" else message
    }

    fun handleException(exception: Throwable): String {
        val message =  when (exception) {
            is TimeoutException -> "Connection timeout. Please try again"
            is ConnectException -> "Couldn't connect. Please check your internet"
            is SocketTimeoutException -> "Connection timeout. Please check your internet connection"
            is UnknownHostException -> "Couldn't connect to server. Please check your internet connection"
            is HttpException -> exception.message() ?:  "Server returned an error ( Code: ${exception.code()} ). Please try again"
            else -> "An error occurred. Please try again"
        }
        return if (message.length > 100) "Server returned an error. Please try again" else message
    }
}