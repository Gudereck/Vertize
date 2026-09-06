package com.gustavo.financas.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.ui.theme.Hairline
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink38
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Negative
import com.gustavo.financas.ui.theme.Surface
import com.gustavo.financas.ui.theme.SurfaceSoft
import com.gustavo.financas.ui.theme.Warn
import java.text.NumberFormat
import java.util.Locale

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
private const val GAP_GRAUS = 6f
private const val PASSO_STEPPER = 50.0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(viewModel: TransactionViewModel, onBack: () -> Unit) {
    val status by viewModel.statusOrcamentos.collectAsStateWithLifecycle()
    val totalMes = status.sumOf { it.gasto }
    val somaLimites = status.sumOf { it.limite }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = { Text("Categorias e orçamentos", style = MaterialTheme.typography.bodyLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Ink50)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                if (totalMes > 0) {
                    DespesasRing(status = status, total = totalMes, somaLimites = somaLimites)
                } else {
                    Text(
                        text = "Nenhuma despesa este mês ainda. Você já pode definir os limites abaixo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink50,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Limites por categoria",
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            items(status, key = { it.category }) { item ->
                CategoriaRow(
                    status = item,
                    onAjustarLimite = { novoLimite -> viewModel.setOrcamento(item.category, novoLimite) }
                )
                Spacer(Modifier.height(9.dp))
            }
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun DespesasRing(status: List<BudgetStatus>, total: Double, somaLimites: Double) {
    val entradas = status.filter { it.gasto > 0 }.sortedByDescending { it.gasto }

    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Hairline),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 18.dp.toPx()
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    drawArc(
                        color = SurfaceSoft,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    var anguloInicial = -90f
                    entradas.forEach { item ->
                        val fatia = if (total > 0) (item.gasto / total).toFloat() else 0f
                        val sweep = (fatia * 360f) - GAP_GRAUS
                        drawArc(
                            color = categoryVisual(item.category).color,
                            startAngle = anguloInicial,
                            sweepAngle = sweep.coerceAtLeast(0f),
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        anguloInicial += fatia * 360f
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("GASTO NO MÊS", style = MaterialTheme.typography.labelLarge, color = Ink38)
                    Text(currencyFormat.format(total), style = MaterialTheme.typography.headlineSmall, color = Ink)
                    if (somaLimites > 0) {
                        Text(
                            "de ${currencyFormat.format(somaLimites)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Ink38
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            androidx.compose.foundation.layout.FlowRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                entradas.forEach { item ->
                    val pct = if (total > 0) (item.gasto / total * 100).toInt() else 0
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(categoryVisual(item.category).color, CircleShape))
                        Text(
                            "${item.category} · $pct%",
                            style = MaterialTheme.typography.bodySmall,
                            color = Ink,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun CategoriaRow(status: BudgetStatus, onAjustarLimite: (Double) -> Unit) {
    val visual = categoryVisual(status.category)
    val estourou = status.definido && status.percentual >= 1.0
    val proximo = status.definido && status.percentual in 0.85..0.9999

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = if (estourou) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0B3A6)) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (estourou) Color(0xFFFDF7F5) else Surface
        )
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(visual.color, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(status.category.take(2).uppercase(), style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp), color = Color.White)
                }
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(status.category, style = MaterialTheme.typography.bodyLarge, color = Ink)
                    Text(
                        text = if (status.definido) "${currencyFormat.format(status.gasto)} de ${currencyFormat.format(status.limite)}" else currencyFormat.format(status.gasto),
                        style = MaterialTheme.typography.titleSmall,
                        color = Ink50
                    )
                }
                Row {
                    StepperButton("−") { onAjustarLimite((status.limite - PASSO_STEPPER).coerceAtLeast(0.0)) }
                    Spacer(Modifier.width(6.dp))
                    StepperButton("+") { onAjustarLimite(status.limite + PASSO_STEPPER) }
                }
            }

            if (status.definido) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Ink.copy(alpha = 0.08f), RoundedCornerShape(5.dp))
                ) {
                    val cor = when {
                        estourou -> Negative
                        proximo -> Warn
                        else -> visual.color
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(status.percentual.toFloat().coerceIn(0f, 1f))
                            .height(8.dp)
                            .background(cor, RoundedCornerShape(5.dp))
                    )
                }
                if (estourou) {
                    Row(modifier = Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(16.dp).background(Negative, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("!", color = Color.White, style = MaterialTheme.typography.labelSmall)
                        }
                        Text(
                            "Estourou o limite em ${currencyFormat.format(status.gasto - status.limite)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF802D22),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepperButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .border(1.dp, Hairline, RoundedCornerShape(9.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = Ink)
    }
}
