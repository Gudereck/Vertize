package com.gustavo.financas.data

import kotlinx.coroutines.flow.Flow

class BillRepository(private val dao: BillDao) {

    val allBills: Flow<List<Bill>> = dao.getAll()

    suspend fun getActiveOnce(): List<Bill> = dao.getActiveOnce()

    suspend fun insert(bill: Bill) = dao.insert(bill)

    suspend fun update(bill: Bill) = dao.update(bill)

    suspend fun delete(bill: Bill) = dao.delete(bill)
}
