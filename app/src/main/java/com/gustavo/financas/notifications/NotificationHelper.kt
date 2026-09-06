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
import com.gustavo.financas.data.Bill
import java.text.NumberFormat
import java.util.Locale

private const val CHANNEL_ID = "orcamentos"
private const val CHANNEL_ID_CONTAS = "contas"

class NotificationHelper(private val context: Context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Orçamentos",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Avisa quando um orçamento mensal é ultrapassado"
                }
            )
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_CONTAS,
                    "Contas a pagar",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Avisa quando uma conta está prestes a vencer ou vence hoje"
                }
            )
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

    fun notificarConta(bill: Bill, venceHoje: Boolean, diasRestantes: Int) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val titulo = if (venceHoje) "${bill.name} vence hoje" else "${bill.name} vence em $diasRestantes dias"
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_CONTAS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText("Valor: ${currencyFormat.format(bill.amount)}")
            .setAutoCancel(true)
            .build()

        val notificationId = (if (venceHoje) 2_000_000 else 3_000_000) + bill.id.toInt()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}
