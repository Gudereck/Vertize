package com.gustavo.financas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gustavo.financas.ui.theme.Hairline
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.SpaceGrotesk

private val linhas = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
    listOf("00", "0", "⌫")
)

@Composable
fun NumericKeypad(onDigit: (String) -> Unit, onBackspace: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        linhas.forEach { linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                linha.forEach { tecla ->
                    KeypadButton(
                        modifier = Modifier.weight(1f),
                        label = tecla,
                        onClick = { if (tecla == "⌫") onBackspace() else onDigit(tecla) }
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Color(0xFFFAFAFA), RoundedCornerShape(16.dp))
            .border(1.dp, Hairline, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        if (label == "⌫") {
            Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Apagar", tint = Ink)
        } else {
            Text(label, fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 19.sp, color = Ink)
        }
    }
}
