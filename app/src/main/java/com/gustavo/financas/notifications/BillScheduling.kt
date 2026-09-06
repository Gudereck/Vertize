package com.gustavo.financas.notifications

import java.util.Calendar

object BillScheduling {

    private fun startOfDay(cal: Calendar): Calendar = (cal.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    fun nextDueDate(dueDay: Int, from: Calendar = Calendar.getInstance()): Calendar {
        val today = startOfDay(from)
        val candidate = startOfDay(today).apply {
            set(Calendar.DAY_OF_MONTH, dueDay.coerceAtMost(getActualMaximum(Calendar.DAY_OF_MONTH)))
        }
        if (candidate.before(today)) {
            candidate.add(Calendar.MONTH, 1)
            candidate.set(Calendar.DAY_OF_MONTH, dueDay.coerceAtMost(candidate.getActualMaximum(Calendar.DAY_OF_MONTH)))
        }
        return candidate
    }

    fun advanceNotifyDate(due: Calendar, daysBefore: Int): Calendar =
        (due.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, -daysBefore) }

    fun isSameDate(a: Calendar, b: Calendar): Boolean =
        a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)

    fun yearMonthKey(cal: Calendar): String =
        "%04d-%02d".format(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)

    fun daysBetween(from: Calendar, to: Calendar): Int {
        val fromDay = startOfDay(from)
        val toDay = startOfDay(to)
        return ((toDay.timeInMillis - fromDay.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
    }
}
