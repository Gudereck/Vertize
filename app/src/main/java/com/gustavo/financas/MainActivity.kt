package com.gustavo.financas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gustavo.financas.data.AppDatabase
import com.gustavo.financas.data.BudgetRepository
import com.gustavo.financas.data.TransactionRepository
import com.gustavo.financas.ui.AddTransactionScreen
import com.gustavo.financas.ui.CategoriesScreen
import com.gustavo.financas.ui.HomeScreen
import com.gustavo.financas.ui.TransactionViewModel
import com.gustavo.financas.ui.TransactionViewModelFactory
import com.gustavo.financas.ui.theme.FinancasTheme

class MainActivity : ComponentActivity() {

    private val repository by lazy {
        TransactionRepository(AppDatabase.getInstance(applicationContext).transactionDao())
    }

    private val budgetRepository by lazy {
        BudgetRepository(AppDatabase.getInstance(applicationContext).budgetDao())
    }

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(repository, budgetRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onAddClick = { navController.navigate("add") },
                                onEditClick = { transaction -> navController.navigate("edit/${transaction.id}") },
                                onCategoriesClick = { navController.navigate("categories") }
                            )
                        }
                        composable("add") {
                            AddTransactionScreen(
                                onSave = { description, amount, type, category ->
                                    viewModel.addTransaction(description, amount, type, category)
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "edit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getLong("id") ?: 0L
                            val transactions by viewModel.transactions.collectAsStateWithLifecycle()
                            val existing = transactions.find { it.id == id }
                            if (existing != null) {
                                AddTransactionScreen(
                                    existing = existing,
                                    onUpdate = { updated -> viewModel.updateTransaction(updated) },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                        composable("categories") {
                            CategoriesScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
