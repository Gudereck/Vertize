package com.gustavo.financas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import com.gustavo.financas.data.Goal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditGoalScreen(
    existing: Goal? = null,
    onSave: (name: String, icon: String, targetAmount: Double, targetDate: Long) -> Unit = { _, _, _, _ -> },
    onUpdate: (Goal) -> Unit = {},
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var amountText by remember { mutableStateOf(existing?.targetAmount?.toString() ?: "") }
    var icon by remember { mutableStateOf(existing?.icon ?: goalIconOptions.first()) }
    val dataPadrao = existing?.targetDate ?: Calendar.getInstance().apply { add(Calendar.MONTH, 6) }.timeInMillis
    var targetDate by remember { mutableStateOf(dataPadrao) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (existing != null) "Editar meta" else "Nova meta", fontWeight = FontWeight.SemiBold) },
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
            Text(
                text = "Ícone",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(goalIconOptions) { key ->
                    val visual = goalIcon(key)
                    val selecionado = key == icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (selecionado) visual.color else visual.color.copy(alpha = 0.18f),
                                CircleShape
                            )
                            .clickable { icon = key },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            visual.icon,
                            contentDescription = key,
                            tint = if (selecionado) MaterialTheme.colorScheme.background else visual.color
                        )
                    }
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome da meta") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Objetivo") },
                prefix = { Text("R$ ") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            OutlinedTextField(
                value = dateFormat.format(Date(targetDate)),
                onValueChange = {},
                readOnly = true,
                label = { Text("Espero alcançar em") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clickable { showDatePicker = true }
            )

            Button(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    if (name.isNotBlank() && amount != null && amount > 0) {
                        if (existing != null) {
                            onUpdate(existing.copy(name = name, icon = icon, targetAmount = amount, targetDate = targetDate))
                        } else {
                            onSave(name, icon, amount, targetDate)
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

    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = targetDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { targetDate = it }
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
