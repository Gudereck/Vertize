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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.data.Transaction
import com.gustavo.financas.data.TransactionType
import com.gustavo.financas.ui.theme.AccentGreen
import com.gustavo.financas.ui.theme.AccentPurple
import com.gustavo.financas.ui.theme.DespesaColor
import com.gustavo.financas.ui.theme.ReceitaColor
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

private enum class Filtro(val rotulo: String) { TODOS("Tudo"), RECEITAS("Receitas"), DESPESAS("Despesas") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TransactionViewModel,
    onEditClick: (Transaction) -> Unit
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Vertize", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            FiltroPills(
                selecionado = filtro,
                onSelecionar = { filtro = it },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

            Text(
                text = "Saldo total",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Text(
                text = currencyFormat.format(saldo),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.ArrowUpward,
                    label = "Receitas",
                    valor = totalReceitas,
                    accent = ReceitaColor
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.ArrowDownward,
                    label = "Despesas",
                    valor = totalDespesas,
                    accent = DespesaColor
                )
            }

            if (variacaoMensal != null) {
                InsightBanner(variacaoMensal = variacaoMensal, modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
            }

            if (listaFiltrada.isEmpty()) {
                EmptyState()
            } else {
                Text(
                    text = "Lançamentos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(listaFiltrada, key = { it.id }) { transaction ->
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

private fun variacaoDespesasMes(transactions: List<Transaction>): Double? {
    val calendar = Calendar.getInstance()
    val mesAtual = calendar.get(Calendar.MONTH)
    val anoAtual = calendar.get(Calendar.YEAR)
    calendar.add(Calendar.MONTH, -1)
    val mesAnterior = calendar.get(Calendar.MONTH)
    val anoMesAnterior = calendar.get(Calendar.YEAR)

    fun totalDoMes(mes: Int, ano: Int): Double {
        val c = Calendar.getInstance()
        return transactions.filter {
            it.type == TransactionType.DESPESA && run {
                c.timeInMillis = it.date
                c.get(Calendar.MONTH) == mes && c.get(Calendar.YEAR) == ano
            }
        }.sumOf { it.amount }
    }

    val despesasAtual = totalDoMes(mesAtual, anoAtual)
    val despesasAnterior = totalDoMes(mesAnterior, anoMesAnterior)
    if (despesasAnterior <= 0.0) return null
    return (despesasAtual - despesasAnterior) / despesasAnterior * 100
}

@Composable
private fun FiltroPills(selecionado: Filtro, onSelecionar: (Filtro) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(Filtro.entries) { opcao ->
            val ativo = opcao == selecionado
            Box(
                modifier = Modifier
                    .background(
                        color = if (ativo) AccentGreen else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onSelecionar(opcao) }
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    text = opcao.rotulo,
                    color = if (ativo) Color.Black else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (ativo) FontWeight.SemiBold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    valor: Double,
    accent: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(accent.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
            }
            Text(
                text = currencyFormat.format(valor),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InsightBanner(variacaoMensal: Double, modifier: Modifier = Modifier) {
    val subiu = variacaoMensal >= 0
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AccentPurple)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${if (subiu) "+" else "-"}${"%.1f".format(abs(variacaoMensal))}%",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (subiu) "Gasto subiu em relação ao mês passado" else "Gasto caiu em relação ao mês passado",
                    color = Color.Black.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(
                imageVector = if (subiu) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
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
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Nenhum lançamento por aqui",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Toque no + para registrar seu salário ou um gasto.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TransactionRow(transaction: Transaction, onEdit: () -> Unit, onDelete: () -> Unit) {
    val valorColor = if (transaction.type == TransactionType.RECEITA) ReceitaColor else DespesaColor
    val sinal = if (transaction.type == TransactionType.RECEITA) "+" else "-"
    val visual = categoryVisual(transaction.category)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onEdit, onLongClick = onDelete)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(visual.icon, contentDescription = null, tint = visual.color, modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = transaction.description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                Text(
                    text = "${transaction.category} • ${dateFormat.format(Date(transaction.date))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "$sinal ${currencyFormat.format(transaction.amount)}",
            color = valorColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
