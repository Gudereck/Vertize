package com.gustavo.financas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.ui.theme.BrandActive
import com.gustavo.financas.ui.theme.Hairline
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink38
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Negative
import com.gustavo.financas.ui.theme.Positive
import com.gustavo.financas.ui.theme.Surface
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: TransactionViewModel, onBack: () -> Unit) {
    val historico by viewModel.historicoMensal.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = { Text("Histórico", style = MaterialTheme.typography.bodyLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Ink50)
                    }
                }
            )
        }
    ) { padding ->
        if (historico.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ainda não há histórico suficiente.", style = MaterialTheme.typography.bodyLarge, color = Ink)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Text(
                        "Saldo dos últimos 6 meses",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink50,
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                    )
                    SaldoMensalChart(historico)
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "DETALHAMENTO",
                        style = MaterialTheme.typography.labelLarge,
                        color = Ink38,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                items(historico.reversed(), key = { "${it.ano}-${it.mes}" }) { mes ->
                    MonthCard(mes)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun SaldoMensalChart(historico: List<MonthSummary>) {
    val maxAbs = historico.maxOf { max(abs(it.saldo), 1.0) }
    val saldoMedio = historico.map { it.saldo }.average()
    val cal = Calendar.getInstance()
    val mesAtualIdx = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Hairline),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 18.dp, bottom = 14.dp)) {
            Text("Saldo médio", style = MaterialTheme.typography.bodySmall, color = Ink50)
            Text(currencyFormat.format(saldoMedio), style = MaterialTheme.typography.titleSmall, color = Ink)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(158.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                historico.forEach { mes ->
                    val ehMesAtual = (mes.ano * 12 + mes.mes) == mesAtualIdx
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            mes.rotulo,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (ehMesAtual) Ink else Ink38
                        )
                        Canvas(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            val fracao = (abs(mes.saldo) / maxAbs).toFloat().coerceIn(0f, 1f)
                            val alturaBarra = size.height * fracao
                            val cor = if (ehMesAtual) BrandActive else BrandActive.copy(alpha = 0.35f)
                            drawRoundRect(
                                color = cor,
                                topLeft = Offset(0f, size.height - alturaBarra),
                                size = Size(size.width, alturaBarra),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx(), 8.dp.toPx())
                            )
                        }
                        Text(
                            text = currencyFormat.format(mes.saldo).replace(",00", ""),
                            style = MaterialTheme.typography.labelLarge,
                            color = Ink,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthCard(mes: MonthSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Hairline),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                mes.rotulo.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                color = Ink,
                modifier = Modifier.width(62.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text("Receitas: ${currencyFormat.format(mes.receitas)}", style = MaterialTheme.typography.bodySmall, color = Positive)
                Text("Despesas: ${currencyFormat.format(mes.despesas)}", style = MaterialTheme.typography.bodySmall, color = Negative)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("SALDO", style = MaterialTheme.typography.labelLarge, color = Ink38)
                Text(
                    currencyFormat.format(mes.saldo),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (mes.saldo >= 0) Positive else Negative
                )
            }
        }
    }
}
