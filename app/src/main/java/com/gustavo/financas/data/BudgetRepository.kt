package com.gustavo.financas.data

import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val dao: BudgetDao) {

    val allBudgets: Flow<List<Budget>> = dao.getAll()

    suspend fun setLimite(category: String, limite: Double) = dao.upsert(Budget(category, limite))

    suspend fun remove(budget: Budget) = dao.delete(budget)
}
