package com.gustavo.financas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.data.GoalDeposit
import com.gustavo.financas.ui.theme.AccentBlue
import com.gustavo.financas.ui.theme.Brand
import com.gustavo.financas.ui.theme.BrandActive
import com.gustavo.financas.ui.theme.Hairline
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink38
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Positive
import com.gustavo.financas.ui.theme.Surface
import com.gustavo.financas.ui.theme.SurfaceSoft
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
private val monthYearFormat = SimpleDateFormat("MMM/yy", Locale("pt", "BR"))

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
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = { Text(detail?.goal?.name ?: "Meta", style = MaterialTheme.typography.bodyLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Ink50)
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(goalId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar meta", tint = Ink50)
                    }
                }
            )
        },
        floatingActionButton = {
            if (tab == GoalTab.DEPOSITOS) {
                FloatingActionButton(
                    onClick = { onAddDepositClick(goalId) },
                    containerColor = Brand,
                    contentColor = Color.White
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
            TabRow(selectedTabIndex = tab.ordinal, containerColor = Surface, contentColor = BrandActive) {
                GoalTab.entries.forEach { opcao ->
                    Tab(
                        selected = tab == opcao,
                        onClick = { tab = opcao },
                        text = { Text(opcao.rotulo, style = MaterialTheme.typography.bodySmall) }
                    )
                }
            }

            when (tab) {
                GoalTab.DETALHES -> DetalhesTab(detail = current, onToggleAchieved = { viewModel.toggleAchieved(current.goal) })
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
private fun DetalhesTab(detail: GoalDetail, onToggleAchieved: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        content = {
            item {
                GoalRing(percent = detail.percent, restante = detail.remaining)
                Text(
                    text = "Objetivo ${currencyFormat.format(detail.goal.targetAmount)} · até ${monthYearFormat.format(Date(detail.goal.targetDate))}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ink50,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    InfoMiniCard(modifier = Modifier.weight(1f), eyebrow = "FALTAM", valor = currencyFormat.format(detail.remaining))
                    InfoMiniCard(modifier = Modifier.weight(1f), eyebrow = "IDEAL POR MÊS", valor = currencyFormat.format(detail.idealPerMonth))
                }

                Spacer(Modifier.height(18.dp))
                Text("Cadastrado em", style = MaterialTheme.typography.bodySmall, color = Ink50)
                Text(dateFormat.format(Date(detail.goal.createdDate)), style = MaterialTheme.typography.bodyLarge, color = Ink)

                Spacer(Modifier.height(18.dp))
                CalculadoraCard(remaining = detail.remaining)

                Spacer(Modifier.height(18.dp))
                AlcancadaRow(alcancada = detail.goal.achieved, onToggle = onToggleAchieved)
                Spacer(Modifier.height(24.dp))
            }
        }
    )
}

@Composable
private fun GoalRing(percent: Double, restante: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(164.dp)) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
            drawArc(
                color = Color(0xFFEDEAFB),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = BrandActive,
                startAngle = -90f,
                sweepAngle = (percent * 360f).toFloat(),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${"%.0f".format(percent * 100)}%", style = MaterialTheme.typography.headlineMedium, color = Ink)
            Text("faltam ${currencyFormat.format(restante)}", style = MaterialTheme.typography.bodySmall, color = Ink38)
        }
    }
}

@Composable
private fun InfoMiniCard(modifier: Modifier = Modifier, eyebrow: String, valor: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Hairline),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Text(eyebrow, style = MaterialTheme.typography.labelLarge, color = Ink38)
            Text(valor, style = MaterialTheme.typography.titleSmall, color = Ink, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun CalculadoraCard(remaining: Double) {
    var mensal by remember { mutableFloatStateOf(300f) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Se eu depositar por mês:", style = MaterialTheme.typography.bodyLarge, color = Ink)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(currencyFormat.format(mensal), style = MaterialTheme.typography.headlineSmall, color = Ink)
                val meses = if (mensal > 0) kotlin.math.ceil(remaining / mensal).toInt() else 0
                val dataPrevista = java.util.Calendar.getInstance().apply { add(java.util.Calendar.MONTH, meses) }
                if (remaining > 0 && meses > 0) {
                    Text(monthYearFormat.format(dataPrevista.time), style = MaterialTheme.typography.bodyMedium, color = AccentBlue)
                }
            }
            Slider(
                value = mensal,
                onValueChange = { mensal = it },
                valueRange = 100f..3000f,
                steps = ((3000f - 100f) / 50f).toInt() - 1,
                colors = SliderDefaults.colors(thumbColor = AccentBlue, activeTrackColor = AccentBlue)
            )
            val meses = if (mensal > 0) kotlin.math.ceil(remaining / mensal).toInt() else 0
            if (remaining > 0) {
                Text(
                    "Nesse ritmo faltam $meses depósitos para chegar aos ${currencyFormat.format(remaining)}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Ink50
                )
            }
        }
    }
}

@Composable
private fun AlcancadaRow(alcancada: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (alcancada) Color(0xFFF7FDF9) else Surface, RoundedCornerShape(18.dp))
            .clickable(onClick = onToggle)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(2.dp, if (alcancada) Positive else Hairline, RoundedCornerShape(7.dp))
                .background(if (alcancada) Positive else Color.Transparent, RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (alcancada) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
        Text("Alcançada", style = MaterialTheme.typography.bodyLarge, color = Ink, modifier = Modifier.padding(start = 10.dp))
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
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nenhum depósito ainda. Toque no + para registrar o primeiro.",
                style = MaterialTheme.typography.bodyMedium,
                color = Ink50
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        content = {
            item {
                Spacer(Modifier.height(16.dp))
                DepositosMensalChart(depositsPorMes)
                Spacer(Modifier.height(16.dp))
                Text("Depósitos", style = MaterialTheme.typography.titleMedium, color = Ink, modifier = Modifier.padding(bottom = 8.dp))
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
        border = BorderStroke(1.dp, Hairline),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Depósitos por mês", style = MaterialTheme.typography.bodyMedium, color = Ink50)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(96.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                historico.forEach { mes ->
                    Column(
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(currencyFormat.format(mes.total).replace(",00", ""), style = MaterialTheme.typography.labelLarge, color = Ink)
                        Canvas(modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp)) {
                            val fracao = (mes.total / maxValor).toFloat().coerceIn(0f, 1f)
                            val alturaBarra = size.height * fracao
                            drawRoundRect(
                                color = Color(0xFF9E7DDB),
                                topLeft = Offset(0f, size.height - alturaBarra),
                                size = Size(size.width, alturaBarra),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(7.dp.toPx(), 7.dp.toPx())
                            )
                        }
                        Text(mes.rotulo, style = MaterialTheme.typography.bodySmall, color = Ink38)
                    }
                }
            }
        }
    }
}

@Composable
private fun DepositRow(deposito: GoalDeposit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(dateFormat.format(Date(deposito.date)), style = MaterialTheme.typography.bodyMedium, color = Ink.copy(alpha = 0.6f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(currencyFormat.format(deposito.amount), style = MaterialTheme.typography.titleSmall, color = Ink)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, contentDescription = "Excluir depósito", tint = Ink38)
            }
        }
    }
}
