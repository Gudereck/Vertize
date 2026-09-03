package com.gustavo.financas.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.gustavo.financas.ui.theme.AccentGreen
import com.gustavo.financas.ui.theme.AccentPurple
import com.gustavo.financas.ui.theme.AccentTeal

data class GoalVisual(val icon: ImageVector, val color: Color)

val goalIconOptions = listOf("casa", "viagem", "carro", "educacao", "emergencia", "eletronico", "outro")

fun goalIcon(key: String): GoalVisual = when (key) {
    "casa" -> GoalVisual(Icons.Default.Home, AccentPurple)
    "viagem" -> GoalVisual(Icons.Default.Flight, AccentTeal)
    "carro" -> GoalVisual(Icons.Default.DirectionsCar, AccentGreen)
    "educacao" -> GoalVisual(Icons.Default.School, Color(0xFF283593))
    "emergencia" -> GoalVisual(Icons.Default.HealthAndSafety, Color(0xFFC62828))
    "eletronico" -> GoalVisual(Icons.Default.Devices, Color(0xFF00838F))
    else -> GoalVisual(Icons.Default.Savings, Color(0xFF616161))
}
