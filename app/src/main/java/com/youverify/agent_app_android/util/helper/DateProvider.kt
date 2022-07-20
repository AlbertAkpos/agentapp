package com.youverify.agent_app_android.util.helper

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

class DateProvider @Inject constructor() {
    private val local = Locale.getDefault()

    @SuppressLint("ConstantLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    private val firstDayOfWeek = WeekFields.of(local).firstDayOfWeek

    @RequiresApi(Build.VERSION_CODES.O)
    private val lastDayOfWeek =
        DayOfWeek.of(((firstDayOfWeek.value + 5) % DayOfWeek.values().size) + 1)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekFirstDate(): LocalDate {
        return LocalDate.now(TimeZone.getDefault().toZoneId())
            .with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekLastDay(): LocalDate {
        return LocalDate.now(TimeZone.getDefault().toZoneId())
            .with(TemporalAdjusters.nextOrSame(lastDayOfWeek))
    }

    private var calendar = Calendar.getInstance()
    private var monthCalendar = Calendar.getInstance()

    fun getLastDayOfPreviousWeek(): Date {
        calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        return calendar.time
    }

    fun getFirstDayPreviousWeek(): Date {
        getLastDayOfPreviousWeek()
        calendar.add(Calendar.DATE, -6)
        return calendar.time
    }

    fun getPreviousMonthFirstDate(): Date {
        monthCalendar = Calendar.getInstance()
        monthCalendar.add(Calendar.MONTH, -1)
        monthCalendar.set(Calendar.MONTH, 1)
        return monthCalendar.time
    }

    fun getPreviousMonthLastDate(): Date {
        getPreviousMonthFirstDate()
        monthCalendar.set(Calendar.DATE, monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return monthCalendar.time
    }

    fun getYesterdayDate(): Date {
        val calender = Calendar.getInstance()
        calender.add(Calendar.DAY_OF_WEEK, -1)
        return calender.time
    }

    fun getFirstDateOfCurrentMonth(): Date {
        val calender = Calendar.getInstance()
        calender.set(Calendar.DAY_OF_MONTH, 1)
        return calender.time
    }

    fun getFirstDateOfCurrentWeek(): Date {
        val calender = Calendar.getInstance()
        calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return calender.time
    }


}