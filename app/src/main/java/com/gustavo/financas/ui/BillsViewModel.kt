package com.gustavo.financas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gustavo.financas.data.Bill
import com.gustavo.financas.data.BillRepository
import com.gustavo.financas.notifications.BillScheduling
import java.util.Calendar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BillStatus(val bill: Bill, val nextDueDate: Long, val diasRestantes: Int)

class BillsViewModel(private val repository: BillRepository) : ViewModel() {

    val billsStatus: StateFlow<List<BillStatus>> = repository.allBills
        .map { bills ->
            val hoje = Calendar.getInstance()
            bills.map { bill ->
                val due = BillScheduling.nextDueDate(bill.dueDay, hoje)
                BillStatus(bill, due.timeInMillis, BillScheduling.daysBetween(hoje, due))
            }.sortedBy { it.nextDueDate }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addBill(name: String, amount: Double, dueDay: Int, reminderDaysBefore: Int) {
        viewModelScope.launch {
            repository.insert(Bill(name = name, amount = amount, dueDay = dueDay, reminderDaysBefore = reminderDaysBefore))
        }
    }

    fun updateBill(bill: Bill) {
        viewModelScope.launch { repository.update(bill) }
    }

    fun deleteBill(bill: Bill) {
        viewModelScope.launch { repository.delete(bill) }
    }

    fun toggleActive(bill: Bill) {
        viewModelScope.launch { repository.update(bill.copy(active = !bill.active)) }
    }
}

class BillsViewModelFactory(private val repository: BillRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BillsViewModel(repository) as T
    }
}
