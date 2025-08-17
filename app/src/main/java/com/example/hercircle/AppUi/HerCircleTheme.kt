package com.example.hercircle.AppUi

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// You can define your Material3 Typography
private val AppTypography = Typography() // Material3 Typography

// Define Material3 Shapes
private val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun HerCircleTheme(themeMode: String = "system", content: @Composable () -> Unit) {
    val colorScheme = when (themeMode) {
        "light" -> lightColorScheme()
        "dark" -> darkColorScheme()
        else -> if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
