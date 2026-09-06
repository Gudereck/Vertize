package com.gustavo.financas.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.gustavo.financas.ui.theme.CategoriaAluguel
import com.gustavo.financas.ui.theme.CategoriaAssinaturas
import com.gustavo.financas.ui.theme.CategoriaFreelance
import com.gustavo.financas.ui.theme.CategoriaInvestimentos
import com.gustavo.financas.ui.theme.CategoriaLazer
import com.gustavo.financas.ui.theme.CategoriaMercado
import com.gustavo.financas.ui.theme.CategoriaOutros
import com.gustavo.financas.ui.theme.CategoriaPresente
import com.gustavo.financas.ui.theme.CategoriaRestaurante
import com.gustavo.financas.ui.theme.CategoriaSalario
import com.gustavo.financas.ui.theme.CategoriaSaude
import com.gustavo.financas.ui.theme.CategoriaTransporte

data class CategoryVisual(val icon: ImageVector, val color: Color)

fun categoryVisual(category: String): CategoryVisual = when (category) {
    "Aluguel" -> CategoryVisual(Icons.Default.Home, CategoriaAluguel)
    "Mercado" -> CategoryVisual(Icons.Default.ShoppingCart, CategoriaMercado)
    "Transporte" -> CategoryVisual(Icons.Default.DirectionsCar, CategoriaTransporte)
    "Restaurante" -> CategoryVisual(Icons.Default.Restaurant, CategoriaRestaurante)
    "Lazer" -> CategoryVisual(Icons.Default.MovieFilter, CategoriaLazer)
    "Saúde" -> CategoryVisual(Icons.Default.LocalHospital, CategoriaSaude)
    "Assinaturas" -> CategoryVisual(Icons.Default.Subscriptions, CategoriaAssinaturas)
    "Salário" -> CategoryVisual(Icons.Default.AttachMoney, CategoriaSalario)
    "Freelance" -> CategoryVisual(Icons.Default.Work, CategoriaFreelance)
    "Investimentos" -> CategoryVisual(Icons.Default.TrendingUp, CategoriaInvestimentos)
    "Presente" -> CategoryVisual(Icons.Default.Redeem, CategoriaPresente)
    else -> CategoryVisual(Icons.Default.Category, CategoriaOutros)
}
