package com.gustavo.financas.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gustavo.financas.data.AppDatabase
import java.util.Calendar

class BillReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val billDao = AppDatabase.getInstance(applicationContext).billDao()
        val notificationHelper = NotificationHelper(applicationContext)
        val today = Calendar.getInstance()

        billDao.getActiveOnce().forEach { bill ->
            val due = BillScheduling.nextDueDate(bill.dueDay, today)
            val cycleKey = BillScheduling.yearMonthKey(due)

            if (bill.reminderDaysBefore > 0) {
                val advanceDate = BillScheduling.advanceNotifyDate(due, bill.reminderDaysBefore)
                if (BillScheduling.isSameDate(today, advanceDate) && bill.lastAdvanceNotifiedYearMonth != cycleKey) {
                    notificationHelper.notificarConta(bill, venceHoje = false, diasRestantes = bill.reminderDaysBefore)
                    billDao.update(bill.copy(lastAdvanceNotifiedYearMonth = cycleKey))
                }
            }

            if (BillScheduling.isSameDate(today, due) && bill.lastDueNotifiedYearMonth != cycleKey) {
                notificationHelper.notificarConta(bill, venceHoje = true, diasRestantes = 0)
                billDao.update(bill.copy(lastDueNotifiedYearMonth = cycleKey))
            }
        }

        return Result.success()
    }
}
