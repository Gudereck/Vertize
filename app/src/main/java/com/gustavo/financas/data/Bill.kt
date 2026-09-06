package com.gustavo.financas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amount: Double,
    val dueDay: Int,
    val reminderDaysBefore: Int = 3,
    val active: Boolean = true,
    val lastAdvanceNotifiedYearMonth: String? = null,
    val lastDueNotifiedYearMonth: String? = null
)
