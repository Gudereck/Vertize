package com.gustavo.financas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gustavo.financas.ui.theme.AccentGreen

fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigateTopLevel("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") },
            colors = navBarItemColors()
        )
        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = { navController.navigateTopLevel("history") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Transações") },
            label = { Text("Transações") },
            colors = navBarItemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("add") },
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(AccentGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Novo lançamento", tint = Color.Black)
                }
            },
            label = null,
            colors = navBarItemColors()
        )
        NavigationBarItem(
            selected = currentRoute == "goals",
            onClick = { navController.navigateTopLevel("goals") },
            icon = { Icon(Icons.Default.Savings, contentDescription = "Metas") },
            label = { Text("Metas") },
            colors = navBarItemColors()
        )
        NavigationBarItem(
            selected = currentRoute == "mais",
            onClick = { navController.navigateTopLevel("mais") },
            icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "Mais") },
            label = { Text("Mais") },
            colors = navBarItemColors()
        )
    }
}

@Composable
private fun navBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = AccentGreen,
    selectedTextColor = AccentGreen,
    indicatorColor = AccentGreen.copy(alpha = 0.18f),
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)
