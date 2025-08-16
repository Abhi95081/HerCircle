package com.example.hercircle.AppUi

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


object ReminderScheduler {
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleOnDate(context: Context, date: LocalDate, hour: Int = 9, title: String, message: String) {
        val trigger = date.atTime(LocalTime.of(hour, 0))
        val delayMillis = java.time.ZonedDateTime.of(trigger, ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis()
        if (delayMillis <= 0) return


        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .build()


        val req = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(req)
    }
}