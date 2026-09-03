package com.gustavo.financas.data

import kotlinx.coroutines.flow.Flow

class GoalDepositRepository(private val dao: GoalDepositDao) {

    val allDeposits: Flow<List<GoalDeposit>> = dao.getAll()

    fun getForGoal(goalId: Long): Flow<List<GoalDeposit>> = dao.getForGoal(goalId)

    suspend fun insert(deposit: GoalDeposit) = dao.insert(deposit)

    suspend fun delete(deposit: GoalDeposit) = dao.delete(deposit)
}
