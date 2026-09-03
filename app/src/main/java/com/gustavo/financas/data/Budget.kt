package com.gustavo.financas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey val category: String,
    val limite: Double
)
