package com.example.hercircle.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hercircle.AppUi.CycleMath
import com.example.hercircle.data.Prefs
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }

    val lastPeriod by prefs.lastPeriod.collectAsState(initial = null)
    val cycleLen by prefs.cycleLen.collectAsState(initial = null)
    val periodLen by prefs.periodLen.collectAsState(initial = null)

    var summary by remember { mutableStateOf("Loading…") }
    var progress by remember { mutableStateOf(0f) }
    var status by remember { mutableStateOf("Calculating…") }

    LaunchedEffect(lastPeriod, cycleLen, periodLen) {
        val lp = lastPeriod ?: LocalDate.now().minusDays(28).toEpochDay()
        val cl = cycleLen ?: 28
        val pl = periodLen ?: 5
        val pred = CycleMath.predict(LocalDate.ofEpochDay(lp), cl, pl)

        // Progress in cycle
        val daysPassed = ChronoUnit.DAYS.between(LocalDate.ofEpochDay(lp), LocalDate.now()).toInt()
        progress = (daysPassed.coerceIn(0, cl).toFloat() / cl.toFloat())

        // Status
        status = when {
            LocalDate.now().isBefore(pred.nextPeriodStart) && LocalDate.now()
                .isAfter(pred.fertileWindowStart) &&
                    LocalDate.now().isBefore(pred.fertileWindowEnd) -> "Fertile Window 🌿"
            LocalDate.now() == pred.ovulation -> "Ovulation Day 💧"
            LocalDate.now().isBefore(pred.nextPeriodEnd) &&
                    LocalDate.now().isAfter(pred.nextPeriodStart.minusDays(1)) -> "On Period 🌸"
            else -> "Safe Days ✨"
        }

        summary = buildString {
            appendLine("🌸 Next period: ${pred.nextPeriodStart} → ${pred.nextPeriodEnd}")
            appendLine("💧 Ovulation: ${pred.ovulation}")
            appendLine("✨ Fertile window: ${pred.fertileWindowStart} → ${pred.fertileWindowEnd}")
            appendLine("⏳ Days left: ${pred.daysLeft}")
        }
    }

    val greeting = remember { timeGreeting() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFCEFF9), Color(0xFFE1BEE7), Color(0xFFD1C4E9))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Greeting
            Text(
                text = "$greeting 💜",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6A1B9A)
                )
            )

            // Current Status
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE1BEE7)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "📅 Today’s Status",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(status, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF4A148C))
                    Spacer(Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF6A1B9A),
                        trackColor = Color(0xFFCE93D8).copy(alpha = 0.3f)
                    )
                }
            }

            // Cycle Overview
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8BBD0)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "🌙 Your Cycle Overview",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(summary, style = MaterialTheme.typography.bodyLarge)
                }
            }

            // AI Tip
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD1C4E9)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "🤖 AI Tip of the Day",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(aiTip(), style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Self-care
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC5CAE9)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "💖 Self-care Reminder",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Take a deep breath, sip some water, and remind yourself: you’re doing amazing. 🌟",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF4A148C)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun aiTip(): String {
    val today = LocalDate.now().dayOfWeek.value
    return when (today) {
        1, 2 -> "Early week can feel heavy. Gentle stretching & iron-rich foods may help. 🌿"
        3, 4 -> "Mid-week check-in: stay hydrated; track any cramps or mood changes. 💧"
        else -> "Weekend self-care: light walk, warm tea, and good sleep support your cycle. 🌸"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun timeGreeting(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..21 -> "Good Evening"
        else -> "Good Night"
    }
}
