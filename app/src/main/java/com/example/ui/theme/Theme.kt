package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB74D), // Warm Orange
    secondary = Color(0xFF81C784), // Fresh Green
    tertiary = Color(0xFF64B5F6), // Ocean Blue
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color(0xFF422100),
    onSecondary = Color(0xFF00390A),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF57C00),
    secondary = Color(0xFF388E3C),
    tertiary = Color(0xFF1976D2),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(Color.White.value),
    onSecondary = Color(Color.White.value),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun WorldFoodTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Existing typography
        content = content
    )
}
