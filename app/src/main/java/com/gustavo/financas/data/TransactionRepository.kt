package com.gustavo.financas.data

import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {

    val allTransactions: Flow<List<Transaction>> = dao.getAll()

    suspend fun insert(transaction: Transaction) = dao.insert(transaction)

    suspend fun update(transaction: Transaction) = dao.update(transaction)

    suspend fun delete(transaction: Transaction) = dao.delete(transaction)
}
