package com.gustavo.financas.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryVisual(val icon: ImageVector, val color: Color)

fun categoryVisual(category: String): CategoryVisual = when (category) {
    "Salário" -> CategoryVisual(Icons.Default.AttachMoney, Color(0xFF2E7D32))
    "Extra" -> CategoryVisual(Icons.Default.Redeem, Color(0xFF00897B))
    "Alimentação" -> CategoryVisual(Icons.Default.Fastfood, Color(0xFFEF6C00))
    "Transporte" -> CategoryVisual(Icons.Default.DirectionsCar, Color(0xFF1565C0))
    "Moradia" -> CategoryVisual(Icons.Default.Home, Color(0xFF6A1B9A))
    "Cartão" -> CategoryVisual(Icons.Default.CreditCard, Color(0xFF00838F))
    "Lazer" -> CategoryVisual(Icons.Default.MovieFilter, Color(0xFFAD1457))
    "Saúde" -> CategoryVisual(Icons.Default.LocalHospital, Color(0xFFC62828))
    "Educação" -> CategoryVisual(Icons.Default.School, Color(0xFF283593))
    else -> CategoryVisual(Icons.Default.Category, Color(0xFF616161))
}
