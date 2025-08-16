package com.example.hercircle.AppUi

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.ChronoUnit


object CycleMath {
    data class Prediction(
        val nextPeriodStart: LocalDate,
        val nextPeriodEnd: LocalDate,
        val ovulation: LocalDate,
        val fertileWindowStart: LocalDate,
        val fertileWindowEnd: LocalDate,
        val daysLeft: Long
    )


    @RequiresApi(Build.VERSION_CODES.O)
    fun predict(lastPeriod: LocalDate, cycleLen: Int = 28, periodLen: Int = 5, today: LocalDate = LocalDate.now()): Prediction {
        require(cycleLen in 21..45) { "cycleLen should be 21..45" }
        require(periodLen in 3..8) { "periodLen should be 3..8" }


// Find the next period start >= today
        var start = lastPeriod
        while (!start.isAfter(today)) {
            start = start.plusDays(cycleLen.toLong())
            if (!start.isAfter(today) && ChronoUnit.DAYS.between(start, today) == 0L) break
        }
        if (start.isBefore(today)) start = today


        val end = start.plusDays(periodLen.toLong() - 1)
        val ovulation = start.minusDays(14)
        val fertileStart = ovulation.minusDays(2)
        val fertileEnd = ovulation.plusDays(2)
        val daysLeft = ChronoUnit.DAYS.between(today, start).coerceAtLeast(0)


        return Prediction(start, end, ovulation, fertileStart, fertileEnd, daysLeft)
    }
}

