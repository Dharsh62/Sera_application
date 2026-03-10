package com.example.kotlinbasicsapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PremiumDarkScheme = darkColorScheme(
    primary = TechBluePrimary,
    secondary = TechBlueAccent,
    background = DeepNavy,
    surface = NavySurface,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun KotlinBasicsAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PremiumDarkScheme,
        typography = Typography,
        content = content
    )
}