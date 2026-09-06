package com.gustavo.financas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gustavo.financas.data.Bill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBillScreen(
    existing: Bill? = null,
    onSave: (name: String, amount: Double, dueDay: Int, reminderDaysBefore: Int) -> Unit = { _, _, _, _ -> },
    onUpdate: (Bill) -> Unit = {},
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var amountText by remember { mutableStateOf(existing?.amount?.toString() ?: "") }
    var dueDayText by remember { mutableStateOf(existing?.dueDay?.toString() ?: "") }
    var reminderText by remember { mutableStateOf(existing?.reminderDaysBefore?.toString() ?: "3") }
    var active by remember { mutableStateOf(existing?.active ?: true) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (existing != null) "Editar conta" else "Nova conta", fontWeight = FontWeight.SemiBold) },
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome da conta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
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

            OutlinedTextField(
                value = dueDayText,
                onValueChange = { dueDayText = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("Dia do vencimento (1-31)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            OutlinedTextField(
                value = reminderText,
                onValueChange = { reminderText = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("Avisar quantos dias antes (0 = só no dia)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ativa", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                Switch(checked = active, onCheckedChange = { active = it })
            }

            Button(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    val dueDay = dueDayText.toIntOrNull()?.coerceIn(1, 31)
                    val reminderDays = reminderText.toIntOrNull() ?: 0
                    if (name.isNotBlank() && amount != null && amount > 0 && dueDay != null) {
                        if (existing != null) {
                            onUpdate(
                                existing.copy(
                                    name = name,
                                    amount = amount,
                                    dueDay = dueDay,
                                    reminderDaysBefore = reminderDays,
                                    active = active
                                )
                            )
                        } else {
                            onSave(name, amount, dueDay, reminderDays)
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
