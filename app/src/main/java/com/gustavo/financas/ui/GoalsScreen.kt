package com.gustavo.financas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.ui.theme.Brand
import com.gustavo.financas.ui.theme.Hairline
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink38
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Positive
import com.gustavo.financas.ui.theme.Surface
import java.text.NumberFormat
import java.util.Locale

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel,
    onAddClick: () -> Unit,
    onGoalClick: (Long) -> Unit
) {
    val goalsWithProgress by viewModel.goalsWithProgress.collectAsStateWithLifecycle()
    val totalGuardado = goalsWithProgress.sumOf { it.totalDeposited }

    Scaffold(containerColor = Surface) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Metas", style = MaterialTheme.typography.titleLarge, color = Ink)
                    Text(
                        text = "${currencyFormat.format(totalGuardado)} guardados em ${goalsWithProgress.size} ${if (goalsWithProgress.size == 1) "meta" else "metas"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink50
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Brand, RoundedCornerShape(13.dp))
                        .clickable(onClick = onAddClick)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text("Nova", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
            }

            if (goalsWithProgress.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Savings, contentDescription = null, modifier = Modifier.size(56.dp), tint = Ink38)
                    Text(
                        text = "Nenhuma meta ainda",
                        style = MaterialTheme.typography.titleMedium,
                        color = Ink,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "Toque em Nova para criar sua primeira meta de economia.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink50,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(goalsWithProgress, key = { it.goal.id }) { item ->
                        GoalCard(item = item, onClick = { onGoalClick(item.goal.id) })
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalCard(item: GoalProgress, onClick: () -> Unit) {
    val alcancada = item.goal.achieved
    val corPrincipal = if (alcancada) Positive else Brand

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Hairline),
        colors = CardDefaults.cardColors(containerColor = if (alcancada) Color(0xFFF7FDF9) else Surface)
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            MiniRing(percent = item.percent, color = corPrincipal, size = 56)
            Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(item.goal.name, style = MaterialTheme.typography.titleMedium, color = Ink)
                    if (alcancada) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .background(Color(0xFFDDF3E2), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("ALCANÇADA", style = MaterialTheme.typography.labelSmall, color = Color(0xFF1F6B41))
                        }
                    }
                }
                Text(
                    text = "${currencyFormat.format(item.totalDeposited)} de ${currencyFormat.format(item.goal.targetAmount)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink50,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .background(Ink.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(item.percent.toFloat().coerceIn(0f, 1f))
                            .height(7.dp)
                            .background(corPrincipal, RoundedCornerShape(4.dp))
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (alcancada) "Meta concluída" else "Faltam ${currencyFormat.format((item.goal.targetAmount - item.totalDeposited).coerceAtLeast(0.0))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Ink50
                )
            }
        }
    }
}

@Composable
fun MiniRing(percent: Double, color: Color, size: Int) {
    Box(modifier = Modifier.size(size.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 6.dp.toPx()
            val arcSize = androidx.compose.ui.geometry.Size(this.size.width - strokeWidth, this.size.height - strokeWidth)
            val topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2, strokeWidth / 2)
            drawArc(
                color = color.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = (percent * 360f).toFloat(),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
        Text("${"%.0f".format(percent * 100)}%", style = MaterialTheme.typography.titleSmall, color = color)
    }
}
