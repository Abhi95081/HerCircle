package com.example.hercircle.screens

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hercircle.AppUi.CycleMath
import com.example.hercircle.AppUi.ReminderScheduler
import com.example.hercircle.data.Prefs
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(onComplete: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }
    val scope = rememberCoroutineScope()

    var lastDate by remember { mutableStateOf(LocalDate.now()) }
    var cycleLen by remember { mutableIntStateOf(28) }
    var periodLen by remember { mutableIntStateOf(5) }

    val showPicker = {
        val d = lastDate
        DatePickerDialog(context, { _, y, m, day ->
            lastDate = LocalDate.of(y, m + 1, day)
        }, d.year, d.monthValue - 1, d.dayOfMonth).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFCEFF9), Color(0xFFE1BEE7), Color(0xFFD1C4E9))
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            "Setup Your Cycle",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6A1B9A)
        )
        Text(
            "Personalize your experience with a few quick details 💜",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(8.dp))

        // Last period picker
        SetupCard(
            icon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF8E24AA)) },
            title = "Last period start",
            content = {
                OutlinedButton(onClick = showPicker, shape = RoundedCornerShape(14.dp)) {
                    Text("$lastDate")
                }
            }
        )

        // Cycle length
        SetupCard(
            icon = { Icon(Icons.Default.Face, contentDescription = null, tint = Color(0xFF8E24AA)) },
            title = "Average cycle length",
            content = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$cycleLen days", fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = cycleLen.toFloat(),
                        onValueChange = { cycleLen = it.toInt() },
                        valueRange = 21f..45f,
                        steps = 45 - 21,
                        colors = SliderDefaults.colors(thumbColor = Color(0xFF8E24AA))
                    )
                }
            }
        )

        // Period length
        SetupCard(
            icon = { Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFF8E24AA)) },
            title = "Period length",
            content = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$periodLen days", fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = periodLen.toFloat(),
                        onValueChange = { periodLen = it.toInt() },
                        valueRange = 3f..8f,
                        steps = 8 - 3,
                        colors = SliderDefaults.colors(thumbColor = Color(0xFF8E24AA))
                    )
                }
            }
        )

        Spacer(Modifier.height(24.dp))

        // Save button
        Button(
            onClick = {
                scope.launch {
                    prefs.savePeriodData(lastDate.toEpochDay(), cycleLen, periodLen)
                    // Schedule reminders
                    val p = CycleMath.predict(lastDate, cycleLen, periodLen)
                    ReminderScheduler.scheduleOnDate(
                        context, p.nextPeriodStart.minusDays(1), 9,
                        "Period starts tomorrow", "Pack essentials and take care 💜"
                    )
                    ReminderScheduler.scheduleOnDate(
                        context, p.nextPeriodStart, 9,
                        "Period starts today", "Stay comfy and hydrated ✨"
                    )
                    onComplete()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
        ) {
            Text("Save & Continue", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun SetupCard(icon: @Composable () -> Unit, title: String, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon()
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            content()
        }
    }
}
