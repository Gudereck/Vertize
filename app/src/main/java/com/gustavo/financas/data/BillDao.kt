package com.gustavo.financas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Query("SELECT * FROM bills ORDER BY dueDay ASC")
    fun getAll(): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE active = 1")
    suspend fun getActiveOnce(): List<Bill>

    @Insert
    suspend fun insert(bill: Bill)

    @Update
    suspend fun update(bill: Bill)

    @Delete
    suspend fun delete(bill: Bill)
}
