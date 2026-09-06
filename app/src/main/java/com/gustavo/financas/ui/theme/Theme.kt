package com.gustavo.financas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColors = lightColorScheme(
    primary = Brand,
    onPrimary = Color.White,
    background = Surface,
    onBackground = Ink,
    surface = SurfaceSoft,
    onSurface = Ink,
    surfaceVariant = SurfaceDarkElevated,
    onSurfaceVariant = Ink50,
    primaryContainer = SurfaceSoft,
    onPrimaryContainer = Ink,
    secondaryContainer = SurfaceDarkElevated,
    onSecondaryContainer = Ink,
    outline = Hairline
)

@Composable
fun FinancasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = Typography,
        content = content
    )
}
