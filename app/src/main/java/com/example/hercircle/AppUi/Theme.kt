package com.example.hercircle.AppUi

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF8E24AA),
    secondary = Color(0xFFBA68C8),
    tertiary = Color(0xFFF48FB1)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD05CE3),
    secondary = Color(0xFFF48FB1),
    tertiary = Color(0xFFCE93D8)
)

// Define your Typography (use defaults or customize)
private val AppTypography = Typography()

// Define your Shapes (use defaults or customize)
private val AppShapes = Shapes()

@Composable
fun HerCircleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
