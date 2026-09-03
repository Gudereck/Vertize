package com.gustavo.financas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String,
    val targetAmount: Double,
    val targetDate: Long,
    val createdDate: Long = System.currentTimeMillis(),
    val achieved: Boolean = false
)
