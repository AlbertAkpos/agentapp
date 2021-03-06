package com.youverify.agent_app_android.util.helper

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit

fun createMultipart(file: File, mimetype: String = "image/png"): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        "files",
        file.name,
        file.asRequestBody(mimetype.toMediaTypeOrNull())
    )
}

fun getDateInMilliSecond(dateString: String): Long? {
    val desiredFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    try {
        val date = desiredFormat.parse(dateString)
        return date.getTime()
    } catch (e: Exception) {
        Timber.d("Exception: $e")
        e.printStackTrace()
    }

    return null
}

fun getTimePassedSinceDate(date: Long) =
    DateUtils.getRelativeTimeSpanString(date, Date().time, DateUtils.MINUTE_IN_MILLIS).toString()

fun isOreoPlus(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun isLollipopPlus(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

fun formatDate(date: Date, output: String = "yyyy-MM-dd"): String {
    val formatter = SimpleDateFormat(output, Locale.ENGLISH)
    return formatter.format(date)
}

fun getDateFromDayAgo(date: Date = Date(), daysAgo: Int): Date {
    val calender = Calendar.getInstance()
    calender.time = date
    val negate = Math.negateExact(daysAgo)
    Timber.d("Negate: $negate")
    calender.add(Calendar.DAY_OF_YEAR, negate)
    return calender.time
}

fun getDate(milliSeconds: Long, dateFormat: String = "dd MMMM yyyy"): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}


fun getDate(milliSeconds: Long): Date {
    val calender = Calendar.getInstance()
    calender.timeInMillis = milliSeconds
    return calender.time
}

fun getDate(dateString: String, dateFormat: String = "yyyy-MM-dd"): Date? {
    return runCatching {
        SimpleDateFormat(dateFormat, Locale.getDefault()).parse(dateString)
    }.getOrNull()
}

fun formatAsTime(milliSeconds: Long): String {
    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(milliSeconds),
        TimeUnit.MILLISECONDS.toMinutes(milliSeconds) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds))
    )
}
