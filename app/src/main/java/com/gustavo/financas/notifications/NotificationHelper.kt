package com.gustavo.financas.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.NumberFormat
import java.util.Locale

private const val CHANNEL_ID = "orcamentos"

class NotificationHelper(private val context: Context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Orçamentos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Avisa quando um orçamento mensal é ultrapassado"
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    fun notificarEstouro(categoria: String, gasto: Double, limite: Double) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle("Orçamento de $categoria estourou")
            .setContentText("Você gastou ${currencyFormat.format(gasto)} de ${currencyFormat.format(limite)} neste mês.")
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(categoria.hashCode(), notification)
    }
}
