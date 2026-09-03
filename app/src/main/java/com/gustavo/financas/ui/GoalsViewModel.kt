package com.gustavo.financas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gustavo.financas.data.Goal
import com.gustavo.financas.data.GoalDeposit
import com.gustavo.financas.data.GoalDepositRepository
import com.gustavo.financas.data.GoalRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.ceil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GoalProgress(val goal: Goal, val totalDeposited: Double) {
    val percent: Double get() = if (goal.targetAmount > 0) (totalDeposited / goal.targetAmount).coerceIn(0.0, 1.0) else 0.0
}

data class GoalDetail(val goal: Goal, val deposits: List<GoalDeposit>, val totalDeposited: Double) {
    val percent: Double get() = if (goal.targetAmount > 0) (totalDeposited / goal.targetAmount).coerceIn(0.0, 1.0) else 0.0
    val remaining: Double get() = (goal.targetAmount - totalDeposited).coerceAtLeast(0.0)
    val monthsRemaining: Int get() = monthsUntil(goal.targetDate)
    val idealPerMonth: Double get() = if (monthsRemaining > 0) remaining / monthsRemaining else remaining
}

data class GoalMonthDeposit(val ano: Int, val mes: Int, val total: Double) {
    val rotulo: String get() {
        val cal = Calendar.getInstance()
        cal.set(ano, mes, 1)
        return SimpleDateFormat("MMM/yy", Locale("pt", "BR")).format(cal.time)
    }
}

private fun monthsUntil(targetDate: Long): Int {
    val agora = Calendar.getInstance()
    val alvo = Calendar.getInstance().apply { timeInMillis = targetDate }
    var meses = (alvo.get(Calendar.YEAR) - agora.get(Calendar.YEAR)) * 12 +
        (alvo.get(Calendar.MONTH) - agora.get(Calendar.MONTH))
    if (alvo.get(Calendar.DAY_OF_MONTH) < agora.get(Calendar.DAY_OF_MONTH)) meses -= 1
    return meses.coerceAtLeast(0)
}

class GoalsViewModel(
    private val goalRepository: GoalRepository,
    private val depositRepository: GoalDepositRepository
) : ViewModel() {

    val goals: StateFlow<List<Goal>> = goalRepository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val allDeposits: StateFlow<List<GoalDeposit>> = depositRepository.allDeposits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goalsWithProgress: StateFlow<List<GoalProgress>> = combine(goals, allDeposits) { goals, deposits ->
        goals.map { goal ->
            GoalProgress(goal, deposits.filter { it.goalId == goal.id }.sumOf { it.amount })
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val selectedGoalId = MutableStateFlow<Long?>(null)

    fun selectGoal(id: Long) {
        selectedGoalId.value = id
    }

    val selectedGoalDetail: StateFlow<GoalDetail?> = combine(selectedGoalId, goals, allDeposits) { id, goals, deposits ->
        val goal = goals.find { it.id == id } ?: return@combine null
        val goalDeposits = deposits.filter { it.goalId == id }.sortedByDescending { it.date }
        GoalDetail(goal, goalDeposits, goalDeposits.sumOf { it.amount })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val depositsPorMes: StateFlow<List<GoalMonthDeposit>> = combine(selectedGoalId, allDeposits) { id, deposits ->
        val cal = Calendar.getInstance()
        deposits.filter { it.goalId == id }
            .groupBy { deposito ->
                cal.timeInMillis = deposito.date
                cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH)
            }
            .map { (chave, itens) -> GoalMonthDeposit(chave.first, chave.second, itens.sumOf { it.amount }) }
            .sortedWith(compareBy({ it.ano }, { it.mes }))
            .takeLast(6)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun calcularProjecao(remaining: Double, monthlyAmount: Double): Pair<Int, Long>? {
        if (monthlyAmount <= 0.0 || remaining <= 0.0) return null
        val meses = ceil(remaining / monthlyAmount).toInt()
        val dataProjetada = Calendar.getInstance().apply { add(Calendar.MONTH, meses) }.timeInMillis
        return meses to dataProjetada
    }

    fun addGoal(name: String, icon: String, targetAmount: Double, targetDate: Long) {
        viewModelScope.launch {
            goalRepository.insert(Goal(name = name, icon = icon, targetAmount = targetAmount, targetDate = targetDate))
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch { goalRepository.update(goal) }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch { goalRepository.delete(goal) }
    }

    fun toggleAchieved(goal: Goal) {
        viewModelScope.launch { goalRepository.update(goal.copy(achieved = !goal.achieved)) }
    }

    fun addDeposit(goalId: Long, amount: Double, date: Long) {
        viewModelScope.launch { depositRepository.insert(GoalDeposit(goalId = goalId, amount = amount, date = date)) }
    }

    fun deleteDeposit(deposit: GoalDeposit) {
        viewModelScope.launch { depositRepository.delete(deposit) }
    }
}

class GoalsViewModelFactory(
    private val goalRepository: GoalRepository,
    private val depositRepository: GoalDepositRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GoalsViewModel(goalRepository, depositRepository) as T
    }
}
