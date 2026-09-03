package com.gustavo.financas.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.financas.ui.theme.DespesaColor
import java.text.NumberFormat
import java.util.Locale

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
private const val GAP_GRAUS = 6f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(viewModel: TransactionViewModel, onBack: () -> Unit) {
    val status by viewModel.statusOrcamentos.collectAsStateWithLifecycle()
    val totalMes = status.sumOf { it.gasto }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Categorias e orçamentos", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
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
                    DespesasRing(status = status, total = totalMes)
                } else {
                    Text(
                        text = "Nenhuma despesa este mês ainda. Você já pode definir os limites abaixo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Limites por categoria",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            items(status, key = { it.category }) { item ->
                CategoriaRow(
                    status = item,
                    onSalvarLimite = { novoLimite -> viewModel.setOrcamento(item.category, novoLimite) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun DespesasRing(status: List<BudgetStatus>, total: Double) {
    val entradas = status.filter { it.gasto > 0 }.sortedByDescending { it.gasto }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.minDimension * 0.11f
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
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
            Text(
                text = "Gasto este mês",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = currencyFormat.format(total),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun CategoriaRow(status: BudgetStatus, onSalvarLimite: (Double) -> Unit) {
    val visual = categoryVisual(status.category)
    var texto by remember(status.limite) {
        mutableStateOf(if (status.limite > 0) status.limite.toString() else "")
    }
    val estourou = status.definido && status.percentual >= 1.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(visual.color.copy(alpha = 0.18f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(visual.icon, contentDescription = null, tint = visual.color, modifier = Modifier.size(18.dp))
                }
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(status.category, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        text = "Gasto este mês: ${currencyFormat.format(status.gasto)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (status.definido) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(status.percentual.toFloat().coerceIn(0f, 1f))
                            .height(8.dp)
                            .background(if (estourou) DespesaColor else visual.color, RoundedCornerShape(4.dp))
                    )
                }
                Text(
                    text = "${"%.0f".format(status.percentual * 100)}% de ${currencyFormat.format(status.limite)}" +
                        if (estourou) " • limite estourado" else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (estourou) DespesaColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            val focusManager = LocalFocusManager.current
            fun salvar() {
                val valor = texto.replace(",", ".").toDoubleOrNull()
                if (valor != null && valor > 0) {
                    onSalvarLimite(valor)
                    focusManager.clearFocus()
                }
            }

            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Limite mensal") },
                prefix = { Text("R$ ") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { salvar() }),
                trailingIcon = {
                    IconButton(onClick = { salvar() }) {
                        Icon(Icons.Default.Check, contentDescription = "Salvar limite")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        }
    }
}
