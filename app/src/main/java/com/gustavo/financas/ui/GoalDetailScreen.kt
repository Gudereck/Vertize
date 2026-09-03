package com.gustavo.financas.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.data.GoalDeposit
import com.gustavo.financas.ui.theme.AccentGreen
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

private enum class GoalTab(val rotulo: String) { DETALHES("DETALHES"), DEPOSITOS("DEPÓSITOS") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    viewModel: GoalsViewModel,
    goalId: Long,
    onBack: () -> Unit,
    onEditClick: (Long) -> Unit,
    onAddDepositClick: (Long) -> Unit
) {
    LaunchedEffect(goalId) { viewModel.selectGoal(goalId) }

    val detail by viewModel.selectedGoalDetail.collectAsStateWithLifecycle()
    val depositsPorMes by viewModel.depositsPorMes.collectAsStateWithLifecycle()
    var tab by remember { mutableStateOf(GoalTab.DETALHES) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(detail?.goal?.name ?: "Meta", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(goalId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar meta")
                    }
                }
            )
        },
        floatingActionButton = {
            if (tab == GoalTab.DEPOSITOS) {
                FloatingActionButton(
                    onClick = { onAddDepositClick(goalId) },
                    containerColor = AccentGreen,
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Novo depósito")
                }
            }
        }
    ) { padding ->
        val current = detail
        if (current == null) {
            Box(modifier = Modifier.padding(padding).fillMaxSize())
            return@Scaffold
        }

        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = tab.ordinal, containerColor = MaterialTheme.colorScheme.background) {
                GoalTab.entries.forEach { opcao ->
                    Tab(
                        selected = tab == opcao,
                        onClick = { tab = opcao },
                        text = { Text(opcao.rotulo) }
                    )
                }
            }

            when (tab) {
                GoalTab.DETALHES -> DetalhesTab(
                    detail = current,
                    onToggleAchieved = { viewModel.toggleAchieved(current.goal) },
                    onCalcular = { mensal -> viewModel.calcularProjecao(current.remaining, mensal) }
                )
                GoalTab.DEPOSITOS -> DepositosTab(
                    detail = current,
                    depositsPorMes = depositsPorMes,
                    onDeleteDeposit = { viewModel.deleteDeposit(it) }
                )
            }
        }
    }
}

@Composable
private fun DetalhesTab(
    detail: GoalDetail,
    onToggleAchieved: () -> Unit,
    onCalcular: (Double) -> Pair<Int, Long>?
) {
    val visual = goalIcon(detail.goal.icon)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        content = {
            item {
                GoalRing(percent = detail.percent, color = visual.color, restante = detail.remaining)

                Spacer(Modifier.height(16.dp))
                InfoRow("Objetivo", currencyFormat.format(detail.goal.targetAmount))
                InfoRow("Espero alcançar em", "${dateFormat.format(Date(detail.goal.targetDate))} • faltam ${detail.monthsRemaining} meses")
                InfoRow("Cadastrado em", dateFormat.format(Date(detail.goal.createdDate)))
                InfoRow("Ideal por mês", currencyFormat.format(detail.idealPerMonth))

                Spacer(Modifier.height(16.dp))
                CalculadoraCard(onCalcular = onCalcular)

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = detail.goal.achieved, onCheckedChange = { onToggleAchieved() })
                    Text("Alcançado", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    )
}

@Composable
private fun GoalRing(percent: Double, color: Color, restante: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.4f)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.minDimension * 0.11f
            val diameter = size.minDimension - strokeWidth
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
            drawArc(
                color = color.copy(alpha = 0.18f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = (percent * 360f).toFloat(),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${"%.0f".format(percent * 100)}%",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "faltam ${currencyFormat.format(restante)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun CalculadoraCard(onCalcular: (Double) -> Pair<Int, Long>?) {
    var texto by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<Pair<Int, Long>?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Se eu depositar por mês:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    label = { Text("R$ 0,00") },
                    prefix = { Text("R$ ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    val valor = texto.replace(",", ".").toDoubleOrNull()
                    resultado = if (valor != null) onCalcular(valor) else null
                }) {
                    Text("Calcular")
                }
            }
            resultado?.let { (meses, data) ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Você alcança a meta em $meses meses (${dateFormat.format(Date(data))}).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AccentGreen
                )
            }
        }
    }
}

@Composable
private fun DepositosTab(
    detail: GoalDetail,
    depositsPorMes: List<GoalMonthDeposit>,
    onDeleteDeposit: (GoalDeposit) -> Unit
) {
    if (detail.deposits.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nenhum depósito ainda. Toque no + para registrar o primeiro.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        content = {
            item {
                Spacer(Modifier.height(16.dp))
                DepositosMensalChart(depositsPorMes)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Depósitos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(detail.deposits, key = { it.id }) { deposito ->
                DepositRow(deposito = deposito, onDelete = { onDeleteDeposit(deposito) })
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    )
}

@Composable
private fun DepositosMensalChart(historico: List<GoalMonthDeposit>) {
    if (historico.isEmpty()) return
    val maxValor = historico.maxOf { max(it.total, 1.0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Depósitos por mês",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                historico.forEach { mes ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Canvas(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp)
                        ) {
                            val fracao = (mes.total / maxValor).toFloat().coerceIn(0f, 1f)
                            val alturaBarra = size.height * fracao
                            drawRect(
                                color = AccentGreen,
                                topLeft = Offset(0f, size.height - alturaBarra),
                                size = Size(size.width, alturaBarra)
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = mes.rotulo,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DepositRow(deposito: GoalDeposit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(currencyFormat.format(deposito.amount), style = MaterialTheme.typography.bodyLarge, color = AccentGreen, fontWeight = FontWeight.SemiBold)
            Text(dateFormat.format(Date(deposito.date)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Close, contentDescription = "Excluir depósito", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
