package com.youverify.agent_app_android.util.helper

import android.text.format.DateUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun createMultipart(file: File, mimetype: String = "image/png"): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        "files",
        file.name,
        file.asRequestBody(mimetype.toMediaTypeOrNull())
    )
}

fun getDateInMilliSecond(dateString: String): Long? {
    val desiredFormat = SimpleDateFormat("d MMMM yyyy, hh:mm aa", Locale.getDefault())
    try {
        val date = desiredFormat.parse(dateString)
        return date.getTime()
    } catch (e: Exception) {
       Timber.d("Exception: $e")
        e.printStackTrace()
    }

    return null
}

fun getTimePassedSinceDate(date: Long) = DateUtils.getRelativeTimeSpanString( date, Date().time, DateUtils.MINUTE_IN_MILLIS).toString()