package com.example.hercircle.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(onBack: () -> Unit) {
    val today = LocalDate.now()
    val yearMonth = remember { mutableStateOf(YearMonth.from(today)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp), // 👈 more breathing space
        verticalArrangement = Arrangement.spacedBy(20.dp) // 👈 adds space between sections
    ) {
        // Title (Month + Year)
        Text(
            text = "${yearMonth.value.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.value.year}",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Weekday headers (Sun - Sat)
        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // 👈 spacing above/below row
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF6200EE)
                    )
                )
            }
        }

        // Days grid
        val days = remember(yearMonth.value) { monthDays(yearMonth.value) }
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp), // 👈 extra gap above calendar
            userScrollEnabled = false // lock to 1 month
        ) {
            items(days) { d ->
                val isCurrentMonth = d.month == yearMonth.value.month
                val isToday = d == today

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(6.dp) // 👈 bigger gap between cells
                        .background(
                            when {
                                isToday -> Color(0xFFBB86FC)
                                isCurrentMonth -> Color(0xFFF2E7FE)
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .border(
                            width = if (isToday) 2.dp else 0.dp,
                            color = if (isToday) Color(0xFF6200EE) else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCurrentMonth) d.dayOfMonth.toString() else "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) Color.White else Color.Black
                        )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun monthDays(ym: YearMonth): List<LocalDate> {
    val first = ym.atDay(1)
    val shift = first.dayOfWeek.value % 7 // make Sunday=0
    val start = first.minusDays(shift.toLong())
    return (0 until 42).map { start.plusDays(it.toLong()) }
}
