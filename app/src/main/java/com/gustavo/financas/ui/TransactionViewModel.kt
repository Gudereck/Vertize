package com.gustavo.financas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gustavo.financas.data.BudgetRepository
import com.gustavo.financas.data.Transaction
import com.gustavo.financas.data.TransactionRepository
import com.gustavo.financas.data.TransactionType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BudgetStatus(val category: String, val gasto: Double, val limite: Double) {
    val percentual: Double get() = if (limite > 0) gasto / limite else 0.0
    val definido: Boolean get() = limite > 0
}

data class MonthSummary(val ano: Int, val mes: Int, val receitas: Double, val despesas: Double) {
    val saldo: Double get() = receitas - despesas

    val rotulo: String get() {
        val cal = Calendar.getInstance()
        cal.set(ano, mes, 1)
        return SimpleDateFormat("MMM/yy", Locale("pt", "BR")).format(cal.time)
    }
}

class TransactionViewModel(
    private val repository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val onOrcamentoEstourado: (categoria: String, gasto: Double, limite: Double) -> Unit = { _, _, _ -> }
) : ViewModel() {

    val transactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val saldo: StateFlow<Double> = transactions
        .map { list -> list.sumOf { if (it.type == TransactionType.RECEITA) it.amount else -it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val despesasMesAtualPorCategoria: StateFlow<Map<String, Double>> = transactions
        .map { list ->
            val agora = Calendar.getInstance()
            val mesAtual = agora.get(Calendar.MONTH)
            val anoAtual = agora.get(Calendar.YEAR)
            val cal = Calendar.getInstance()
            list.filter { transacao ->
                if (transacao.type != TransactionType.DESPESA) return@filter false
                cal.timeInMillis = transacao.date
                cal.get(Calendar.MONTH) == mesAtual && cal.get(Calendar.YEAR) == anoAtual
            }.groupBy { it.category }.mapValues { (_, itens) -> itens.sumOf { it.amount } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private val limitesPorCategoria: StateFlow<Map<String, Double>> = budgetRepository.allBudgets
        .map { lista -> lista.associate { it.category to it.limite } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val statusOrcamentos: StateFlow<List<BudgetStatus>> =
        combine(limitesPorCategoria, despesasMesAtualPorCategoria) { limites, gastos ->
            categoriasDespesa.map { categoria ->
                BudgetStatus(
                    category = categoria,
                    gasto = gastos[categoria] ?: 0.0,
                    limite = limites[categoria] ?: 0.0
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historicoMensal: StateFlow<List<MonthSummary>> = transactions
        .map { list ->
            val cal = Calendar.getInstance()
            list.groupBy { transacao ->
                cal.timeInMillis = transacao.date
                cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH)
            }.map { (chave, itens) ->
                MonthSummary(
                    ano = chave.first,
                    mes = chave.second,
                    receitas = itens.filter { it.type == TransactionType.RECEITA }.sumOf { it.amount },
                    despesas = itens.filter { it.type == TransactionType.DESPESA }.sumOf { it.amount }
                )
            }.sortedWith(compareBy({ it.ano }, { it.mes })).takeLast(6)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val categoriasJaNotificadas = mutableSetOf<String>()

    init {
        viewModelScope.launch {
            statusOrcamentos.collect { lista ->
                val cal = Calendar.getInstance()
                val chaveDoMes = "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)}"
                lista.filter { it.definido && it.percentual >= 1.0 }.forEach { status ->
                    val chave = "${status.category}-$chaveDoMes"
                    if (categoriasJaNotificadas.add(chave)) {
                        onOrcamentoEstourado(status.category, status.gasto, status.limite)
                    }
                }
            }
        }
    }

    fun addTransaction(description: String, amount: Double, type: TransactionType, category: String) {
        viewModelScope.launch {
            repository.insert(
                Transaction(
                    description = description,
                    amount = amount,
                    type = type,
                    category = category,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.update(transaction) }
    }

    fun delete(transaction: Transaction) {
        viewModelScope.launch { repository.delete(transaction) }
    }

    fun setOrcamento(category: String, limite: Double) {
        viewModelScope.launch { budgetRepository.setLimite(category, limite) }
    }
}

class TransactionViewModelFactory(
    private val repository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val onOrcamentoEstourado: (categoria: String, gasto: Double, limite: Double) -> Unit = { _, _, _ -> }
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TransactionViewModel(repository, budgetRepository, onOrcamentoEstourado) as T
    }
}
