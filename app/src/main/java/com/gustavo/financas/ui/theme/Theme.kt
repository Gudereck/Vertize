package com.gustavo.financas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = AccentGreen,
    onPrimary = Color.Black,
    background = AppBackground,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    surfaceVariant = SurfaceDarkElevated,
    onSurfaceVariant = TextSecondaryDark,
    primaryContainer = SurfaceDark,
    onPrimaryContainer = Color.White,
    secondaryContainer = SurfaceDarkElevated,
    onSecondaryContainer = Color.White
)

@Composable
fun FinancasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content
    )
}
