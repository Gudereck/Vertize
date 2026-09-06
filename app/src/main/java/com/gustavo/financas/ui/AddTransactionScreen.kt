package com.gustavo.financas.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gustavo.financas.data.Transaction
import com.gustavo.financas.data.TransactionType
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Negative
import com.gustavo.financas.ui.theme.Positive
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTransactionScreen(
    existing: Transaction? = null,
    onSave: (description: String, amount: Double, type: TransactionType, category: String) -> Unit = { _, _, _, _ -> },
    onUpdate: (Transaction) -> Unit = {},
    onDelete: (Transaction) -> Unit = {},
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf(existing?.description ?: "") }
    var amountCents by remember { mutableLongStateOf(((existing?.amount ?: 0.0) * 100).toLong()) }
    var type by remember { mutableStateOf(existing?.type ?: TransactionType.DESPESA) }
    val categorias = if (type == TransactionType.RECEITA) categoriasReceita else categoriasDespesa
    var category by remember { mutableStateOf(existing?.category ?: categorias.first()) }
    var dataMillis by remember { mutableLongStateOf(existing?.date ?: System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val amount = amountCents / 100.0
    val valorColor = if (type == TransactionType.RECEITA) Positive else Ink

    fun salvar() {
        if (amount <= 0) {
            scope.launch { snackbarHostState.showSnackbar("Informe um valor maior que zero para salvar.") }
            return
        }
        val desc = description.ifBlank { category }
        if (existing != null) {
            onUpdate(existing.copy(description = desc, amount = amount, type = type, category = category, date = dataMillis))
        } else {
            onSave(desc, amount, type, category)
        }
        onBack()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (existing != null) "Editar lançamento" else "Novo lançamento", style = MaterialTheme.typography.bodyLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Cancelar", color = Ink50) }
                },
                actions = {
                    TextButton(onClick = { salvar() }) { Text("Salvar") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = type == TransactionType.DESPESA,
                    onClick = {
                        type = TransactionType.DESPESA
                        category = categoriasDespesa.first()
                    },
                    leadingIcon = { Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Negative.copy(alpha = 0.14f),
                        selectedLabelColor = Negative,
                        selectedLeadingIconColor = Negative
                    ),
                    label = { Text("Despesa") }
                )
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = type == TransactionType.RECEITA,
                    onClick = {
                        type = TransactionType.RECEITA
                        category = categoriasReceita.first()
                    },
                    leadingIcon = { Icon(Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Positive.copy(alpha = 0.14f),
                        selectedLabelColor = Positive,
                        selectedLeadingIconColor = Positive
                    ),
                    label = { Text("Receita") }
                )
            }

            Text(
                text = "VALOR",
                style = MaterialTheme.typography.labelLarge,
                color = Ink50,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = currencyFormat.format(amount),
                style = MaterialTheme.typography.displayMedium,
                color = valorColor,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Categoria",
                style = MaterialTheme.typography.bodyMedium,
                color = Ink50,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                categorias.forEach { opcao ->
                    val selecionado = opcao == category
                    val visual = categoryVisual(opcao)
                    FilterChip(
                        selected = selecionado,
                        onClick = { category = opcao },
                        label = { Text(opcao) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = visual.color,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                androidx.compose.material3.OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                androidx.compose.material3.OutlinedTextField(
                    value = dateFormat.format(Date(dataMillis)),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Data") },
                    modifier = Modifier
                        .width(130.dp)
                        .clickable { showDatePicker = true }
                )
            }

            Spacer(Modifier.height(20.dp))
            NumericKeypad(
                onDigit = { d ->
                    val digitos = if (d == "00") 2 else 1
                    val multiplicador = if (digitos == 2) 100L else 10L
                    val novo = amountCents * multiplicador + (if (d == "00") 0 else d.toLong())
                    if (novo < 100_000_000L) amountCents = novo
                },
                onBackspace = { amountCents /= 10 }
            )

            if (existing != null) {
                Spacer(Modifier.height(20.dp))
                OutlinedButton(
                    onClick = { onDelete(existing); onBack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Negative)
                ) {
                    Text("Excluir lançamento")
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = dataMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { dataMillis = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
