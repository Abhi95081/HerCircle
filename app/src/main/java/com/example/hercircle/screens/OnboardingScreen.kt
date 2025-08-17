package com.example.hercircle.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hercircle.R

@Composable
fun OnboardingScreen(onGetStarted: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFCEFF9), Color(0xFFE1BEE7), Color(0xFFD1C4E9))
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // App Logo / Illustration
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with actual image
            contentDescription = "App Logo",
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 24.dp)
        )

        // App Title + Subtitle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "HerCircle",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your AI-powered cycle companion",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Features Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "What you’ll get",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                FeatureItem("Smart period predictions")
                FeatureItem("Ovulation & fertile window tracking")
                FeatureItem("Gentle daily reminders")
                FeatureItem("Private, on-device data")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Get Started Button
        Button(
            onClick = onGetStarted,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
        ) {
            Text("Get Started", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF8E24AA),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
