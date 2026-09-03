package com.gustavo.financas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    RECEITA,
    DESPESA
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val date: Long
)
