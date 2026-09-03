package com.gustavo.financas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColors = lightColorScheme(
    primary = AccentGreen,
    onPrimary = Color.Black,
    background = AppBackground,
    onBackground = TextPrimaryLight,
    surface = SurfaceDark,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceDarkElevated,
    onSurfaceVariant = TextSecondaryDark,
    primaryContainer = SurfaceDark,
    onPrimaryContainer = TextPrimaryLight,
    secondaryContainer = SurfaceDarkElevated,
    onSecondaryContainer = TextPrimaryLight
)

@Composable
fun FinancasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = Typography,
        content = content
    )
}
