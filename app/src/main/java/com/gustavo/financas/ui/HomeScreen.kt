package com.gustavo.financas.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.data.Transaction
import com.gustavo.financas.data.TransactionType
import com.gustavo.financas.ui.theme.Brand
import com.gustavo.financas.ui.theme.BrandCard
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink38
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Positive
import com.gustavo.financas.ui.theme.Surface
import com.gustavo.financas.ui.theme.SurfaceSoft
import com.gustavo.financas.ui.theme.Warn
import com.gustavo.financas.ui.theme.WarnBg
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
private val dayHeaderFormat = SimpleDateFormat("dd 'de' MMMM", Locale("pt", "BR"))
private val monthFormat = SimpleDateFormat("MMMM", Locale("pt", "BR"))

private enum class Filtro(val rotulo: String) { TODOS("Tudo"), RECEITAS("Receitas"), DESPESAS("Despesas") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TransactionViewModel,
    onEditClick: (Transaction) -> Unit,
    onInsightClick: () -> Unit = {}
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val saldo by viewModel.saldo.collectAsStateWithLifecycle()
    var filtro by remember { mutableStateOf(Filtro.TODOS) }

    val totalReceitas = transactions.filter { it.type == TransactionType.RECEITA }.sumOf { it.amount }
    val totalDespesas = transactions.filter { it.type == TransactionType.DESPESA }.sumOf { it.amount }
    val listaFiltrada = when (filtro) {
        Filtro.TODOS -> transactions
        Filtro.RECEITAS -> transactions.filter { it.type == TransactionType.RECEITA }
        Filtro.DESPESAS -> transactions.filter { it.type == TransactionType.DESPESA }
    }
    val variacaoMensal = variacaoDespesasMes(transactions)
    val mesAtual = monthFormat.format(Date()).replaceFirstChar { it.uppercase() }

    Scaffold(containerColor = Surface) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Header()
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(18)
                    SaldoCard(saldo = saldo, mesAtual = mesAtual, receitas = totalReceitas, despesas = totalDespesas)
                    if (variacaoMensal != null) {
                        Spacer(18)
                        InsightBanner(variacaoMensal = variacaoMensal, onClick = onInsightClick)
                    }
                    Spacer(18)
                    FiltroSegmentado(selecionado = filtro, onSelecionar = { filtro = it })
                }
            }

            if (listaFiltrada.isEmpty()) {
                item { EmptyState() }
            } else {
                val agrupado = listaFiltrada.groupBy { diaChave(it.date) }
                agrupado.forEach { (_, itensDoDia) ->
                    item {
                        Text(
                            text = dayHeaderFormat.format(Date(itensDoDia.first().date)),
                            style = MaterialTheme.typography.labelLarge,
                            color = Ink38,
                            modifier = Modifier.padding(start = 20.dp, top = 14.dp, bottom = 8.dp)
                        )
                    }
                    items(itensDoDia, key = { it.id }) { transaction ->
                        TransactionRow(
                            transaction = transaction,
                            onEdit = { onEditClick(transaction) },
                            onDelete = { viewModel.delete(transaction) }
                        )
                    }
                }
            }
        }
    }
}

private fun diaChave(dateMillis: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = dateMillis
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

@Composable
private fun Spacer(dp: Int) = androidx.compose.foundation.layout.Spacer(Modifier.padding(top = dp.dp))

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(Brand, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .rotate(45f)
                        .background(Color.White, RoundedCornerShape(2.dp))
                )
            }
            Text(
                text = "Vertize",
                style = MaterialTheme.typography.titleMedium,
                color = Ink,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Brand.copy(alpha = 0.14f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("G", style = MaterialTheme.typography.bodySmall, color = Brand)
        }
    }
}

@Composable
private fun SaldoCard(saldo: Double, mesAtual: String, receitas: Double, despesas: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = BrandCard)
    ) {
        Column(modifier = Modifier.padding(start = 22.dp, top = 22.dp, end = 22.dp, bottom = 18.dp)) {
            Text(
                text = "SALDO DE ${mesAtual.uppercase()}",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = currencyFormat.format(saldo),
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SubCard(modifier = Modifier.weight(1f), label = "Receitas", valor = receitas, dotColor = Color(0xFFB7F0C6))
                SubCard(modifier = Modifier.weight(1f), label = "Despesas", valor = despesas, dotColor = Color(0xFFF3B3A6))
            }
        }
    }
}

@Composable
private fun SubCard(modifier: Modifier = Modifier, label: String, valor: Double, dotColor: Color) {
    Row(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(horizontal = 13.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(7.dp).background(dotColor, CircleShape))
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.78f))
            Text(currencyFormat.format(valor), style = MaterialTheme.typography.titleSmall, color = Color.White)
        }
    }
}

@Composable
private fun InsightBanner(variacaoMensal: Double, onClick: () -> Unit) {
    val subiu = variacaoMensal >= 0
    val texto = if (subiu) {
        "Você já gastou ${"%.0f".format(abs(variacaoMensal))}% mais que no mesmo período do mês passado."
    } else {
        "Seus gastos estão ${"%.0f".format(abs(variacaoMensal))}% abaixo do mesmo período do mês passado. Bom ritmo."
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(WarnBg, RoundedCornerShape(18.dp))
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(30.dp).background(Warn, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5A4020),
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Text("›", style = MaterialTheme.typography.titleMedium, color = Color(0xFF5A4020))
    }
}

@Composable
private fun FiltroSegmentado(selecionado: Filtro, onSelecionar: (Filtro) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceSoft, RoundedCornerShape(14.dp))
            .padding(4.dp)
    ) {
        Filtro.entries.forEach { opcao ->
            val ativo = opcao == selecionado
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (ativo) Color.White else Color.Transparent, RoundedCornerShape(11.dp))
                    .clickable { onSelecionar(opcao) }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = opcao.rotulo,
                    color = if (ativo) Ink else Ink50,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun variacaoDespesasMes(transactions: List<Transaction>): Double? {
    val calendar = Calendar.getInstance()
    val mesAtual = calendar.get(Calendar.MONTH)
    val anoAtual = calendar.get(Calendar.YEAR)
    val diaAtual = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.add(Calendar.MONTH, -1)
    val mesAnterior = calendar.get(Calendar.MONTH)
    val anoMesAnterior = calendar.get(Calendar.YEAR)

    fun totalDoPeriodo(mes: Int, ano: Int): Double {
        val c = Calendar.getInstance()
        return transactions.filter {
            it.type == TransactionType.DESPESA && run {
                c.timeInMillis = it.date
                c.get(Calendar.MONTH) == mes && c.get(Calendar.YEAR) == ano && c.get(Calendar.DAY_OF_MONTH) <= diaAtual
            }
        }.sumOf { it.amount }
    }

    val despesasAtual = totalDoPeriodo(mesAtual, anoAtual)
    val despesasAnterior = totalDoPeriodo(mesAnterior, anoMesAnterior)
    if (despesasAnterior <= 0.0) return null
    return (despesasAtual - despesasAnterior) / despesasAnterior * 100
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.ReceiptLong,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = Ink38
        )
        Text(
            text = "Nenhum lançamento por aqui",
            style = MaterialTheme.typography.titleMedium,
            color = Ink,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Toque no + para registrar seu salário ou um gasto.",
            style = MaterialTheme.typography.bodyMedium,
            color = Ink50,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TransactionRow(transaction: Transaction, onEdit: () -> Unit, onDelete: () -> Unit) {
    val valorColor = if (transaction.type == TransactionType.RECEITA) Positive else Ink
    val sinal = if (transaction.type == TransactionType.RECEITA) "+" else "−"
    val visual = categoryVisual(transaction.category)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onEdit, onLongClick = onDelete)
            .padding(horizontal = 20.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(visual.color.copy(alpha = 0.14f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.category.take(2).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = visual.color
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = transaction.description, style = MaterialTheme.typography.bodyLarge, color = Ink)
                Text(
                    text = "${transaction.category} • ${dateFormat.format(Date(transaction.date))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Ink50
                )
            }
        }
        Text(
            text = "$sinal ${currencyFormat.format(transaction.amount)}",
            color = valorColor,
            style = MaterialTheme.typography.titleSmall
        )
    }
}
