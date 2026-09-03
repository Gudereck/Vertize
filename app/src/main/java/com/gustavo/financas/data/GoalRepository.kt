package com.gustavo.financas.data

import kotlinx.coroutines.flow.Flow

class GoalRepository(private val dao: GoalDao) {

    val allGoals: Flow<List<Goal>> = dao.getAll()

    suspend fun insert(goal: Goal) = dao.insert(goal)

    suspend fun update(goal: Goal) = dao.update(goal)

    suspend fun delete(goal: Goal) = dao.delete(goal)
}
