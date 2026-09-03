package com.gustavo.financas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDepositDao {

    @Query("SELECT * FROM goal_deposits ORDER BY date DESC")
    fun getAll(): Flow<List<GoalDeposit>>

    @Query("SELECT * FROM goal_deposits WHERE goalId = :goalId ORDER BY date DESC")
    fun getForGoal(goalId: Long): Flow<List<GoalDeposit>>

    @Insert
    suspend fun insert(deposit: GoalDeposit)

    @Delete
    suspend fun delete(deposit: GoalDeposit)
}
