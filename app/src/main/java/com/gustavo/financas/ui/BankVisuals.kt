package com.gustavo.financas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BankVisual(val label: String, val color: Color)

val bankOptions = listOf("nubank", "itau", "inter", "c6", "santander", "sicoob", "outro")

fun bankVisual(key: String): BankVisual = when (key) {
    "nubank" -> BankVisual("Nu", Color(0xFF8A05BE))
    "itau" -> BankVisual("Itaú", Color(0xFFEC7000))
    "inter" -> BankVisual("Inter", Color(0xFFFF7A00))
    "c6" -> BankVisual("C6", Color(0xFF1B1B1B))
    "santander" -> BankVisual("San", Color(0xFFEC0000))
    "sicoob" -> BankVisual("Sicoob", Color(0xFF00995C))
    else -> BankVisual("Banco", Color(0xFF616161))
}

@Composable
fun BankBadge(key: String, size: Int = 40) {
    val visual = bankVisual(key)
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(visual.color, RoundedCornerShape((size * 0.28).dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = visual.label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size * 0.3).sp,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}
