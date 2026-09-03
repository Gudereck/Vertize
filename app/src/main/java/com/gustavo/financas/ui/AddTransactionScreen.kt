package com.gustavo.financas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gustavo.financas.data.Transaction
import com.gustavo.financas.data.TransactionType
import com.gustavo.financas.ui.theme.DespesaColor
import com.gustavo.financas.ui.theme.ReceitaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    existing: Transaction? = null,
    onSave: (description: String, amount: Double, type: TransactionType, category: String) -> Unit = { _, _, _, _ -> },
    onUpdate: (Transaction) -> Unit = {},
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf(existing?.description ?: "") }
    var amountText by remember { mutableStateOf(existing?.amount?.toString() ?: "") }
    var type by remember { mutableStateOf(existing?.type ?: TransactionType.DESPESA) }
    val categorias = if (type == TransactionType.RECEITA) categoriasReceita else categoriasDespesa
    var category by remember { mutableStateOf(existing?.category ?: categorias.first()) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (existing != null) "Editar lançamento" else "Novo lançamento", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = type == TransactionType.RECEITA,
                    onClick = {
                        type = TransactionType.RECEITA
                        category = categoriasReceita.first()
                    },
                    leadingIcon = { Icon(Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ReceitaColor.copy(alpha = 0.18f),
                        selectedLabelColor = ReceitaColor,
                        selectedLeadingIconColor = ReceitaColor
                    ),
                    label = { Text("Receita") }
                )
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = type == TransactionType.DESPESA,
                    onClick = {
                        type = TransactionType.DESPESA
                        category = categoriasDespesa.first()
                    },
                    leadingIcon = { Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DespesaColor.copy(alpha = 0.18f),
                        selectedLabelColor = DespesaColor,
                        selectedLeadingIconColor = DespesaColor
                    ),
                    label = { Text("Despesa") }
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Valor") },
                prefix = { Text("R$ ") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    leadingIcon = {
                        val visual = categoryVisual(category)
                        Icon(visual.icon, contentDescription = null, tint = visual.color)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { opcao ->
                        val visual = categoryVisual(opcao)
                        DropdownMenuItem(
                            text = { Text(opcao) },
                            leadingIcon = { Icon(visual.icon, contentDescription = null, tint = visual.color) },
                            onClick = {
                                category = opcao
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    if (description.isNotBlank() && amount != null && amount > 0) {
                        if (existing != null) {
                            onUpdate(existing.copy(description = description, amount = amount, type = type, category = category))
                        } else {
                            onSave(description, amount, type, category)
                        }
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
                    .height(50.dp)
            ) {
                Text(if (existing != null) "Atualizar" else "Salvar")
            }
        }
    }
}
