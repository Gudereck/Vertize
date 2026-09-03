package com.gustavo.financas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gustavo.financas.data.AppDatabase
import com.gustavo.financas.data.BudgetRepository
import com.gustavo.financas.data.GoalDepositRepository
import com.gustavo.financas.data.GoalRepository
import com.gustavo.financas.data.TransactionRepository
import com.gustavo.financas.notifications.NotificationHelper
import com.gustavo.financas.ui.AddDepositScreen
import com.gustavo.financas.ui.AddEditGoalScreen
import com.gustavo.financas.ui.AddTransactionScreen
import com.gustavo.financas.ui.AppBottomBar
import com.gustavo.financas.ui.CategoriesScreen
import com.gustavo.financas.ui.GoalDetailScreen
import com.gustavo.financas.ui.GoalsScreen
import com.gustavo.financas.ui.GoalsViewModel
import com.gustavo.financas.ui.GoalsViewModelFactory
import com.gustavo.financas.ui.HistoryScreen
import com.gustavo.financas.ui.HomeScreen
import com.gustavo.financas.ui.MaisScreen
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

    private val goalRepository by lazy {
        GoalRepository(AppDatabase.getInstance(applicationContext).goalDao())
    }

    private val goalDepositRepository by lazy {
        GoalDepositRepository(AppDatabase.getInstance(applicationContext).goalDepositDao())
    }

    private val notificationHelper by lazy { NotificationHelper(applicationContext) }

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(
            repository,
            budgetRepository,
            onOrcamentoEstourado = { categoria, gasto, limite ->
                notificationHelper.notificarEstouro(categoria, gasto, limite)
            }
        )
    }

    private val goalsViewModel: GoalsViewModel by viewModels {
        GoalsViewModelFactory(goalRepository, goalDepositRepository)
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            FinancasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.destination?.route
                    val topLevelRoutes = setOf("home", "history", "goals", "mais")

                    Scaffold(
                        bottomBar = {
                            if (currentRoute in topLevelRoutes) {
                                AppBottomBar(navController)
                            }
                        }
                    ) { outerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(outerPadding)
                        ) {
                            composable("home") {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onEditClick = { transaction -> navController.navigate("edit/${transaction.id}") }
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
                            composable("history") {
                                HistoryScreen(
                                    viewModel = viewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("mais") {
                                MaisScreen(
                                    onCategoriesClick = { navController.navigate("categories") }
                                )
                            }
                            composable("goals") {
                                GoalsScreen(
                                    viewModel = goalsViewModel,
                                    onAddClick = { navController.navigate("add_goal") },
                                    onGoalClick = { id -> navController.navigate("goal_detail/$id") }
                                )
                            }
                            composable("add_goal") {
                                AddEditGoalScreen(
                                    onSave = { name, icon, targetAmount, targetDate ->
                                        goalsViewModel.addGoal(name, icon, targetAmount, targetDate)
                                    },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable(
                                route = "edit_goal/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getLong("id") ?: 0L
                                val goals by goalsViewModel.goals.collectAsStateWithLifecycle()
                                val existing = goals.find { it.id == id }
                                if (existing != null) {
                                    AddEditGoalScreen(
                                        existing = existing,
                                        onUpdate = { updated -> goalsViewModel.updateGoal(updated) },
                                        onBack = { navController.popBackStack() }
                                    )
                                }
                            }
                            composable(
                                route = "goal_detail/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getLong("id") ?: 0L
                                GoalDetailScreen(
                                    viewModel = goalsViewModel,
                                    goalId = id,
                                    onBack = { navController.popBackStack() },
                                    onEditClick = { goalId -> navController.navigate("edit_goal/$goalId") },
                                    onAddDepositClick = { goalId -> navController.navigate("add_deposit/$goalId") }
                                )
                            }
                            composable(
                                route = "add_deposit/{goalId}",
                                arguments = listOf(navArgument("goalId") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val goalId = backStackEntry.arguments?.getLong("goalId") ?: 0L
                                AddDepositScreen(
                                    onSave = { amount, date -> goalsViewModel.addDeposit(goalId, amount, date) },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
