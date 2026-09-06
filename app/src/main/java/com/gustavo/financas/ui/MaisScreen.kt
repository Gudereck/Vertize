package com.gustavo.financas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gustavo.financas.ui.theme.Brand
import com.gustavo.financas.ui.theme.BrandActive
import com.gustavo.financas.ui.theme.Hairline
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink38
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Surface
import com.gustavo.financas.ui.theme.SurfaceSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaisScreen(
    statusOrcamentos: List<BudgetStatus> = emptyList(),
    onCategoriesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    val categoriasEstouradas = statusOrcamentos.count { it.definido && it.percentual >= 1.0 }
    val subtitulo = if (categoriasEstouradas > 0) {
        "$categoriasEstouradas ${if (categoriasEstouradas == 1) "categoria" else "categorias"} acima do limite"
    } else {
        "Todos os limites em dia"
    }

    Scaffold(containerColor = Surface) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Text("Mais", style = MaterialTheme.typography.titleLarge, color = Ink, modifier = Modifier.padding(vertical = 16.dp))

            MaisItemAtivo(subtitulo = subtitulo, onClick = onCategoriesClick)
            Spacer(Modifier.height(9.dp))
            MaisItem(icon = Icons.Default.History, label = "Histórico mensal", onClick = onHistoryClick)

            Spacer(Modifier.height(9.dp))
            MaisItemFuturo(icon = Icons.Default.CreditCard, label = "Cartões de crédito", badge = "EM BREVE")
            Spacer(Modifier.height(9.dp))
            MaisItemFuturo(icon = Icons.Default.Download, label = "Exportar dados", badge = null)
            Spacer(Modifier.height(9.dp))
            MaisItemFuturo(icon = Icons.Default.Notifications, label = "Notificações", badge = null)

            Spacer(Modifier.height(20.dp))
            BankIconsCard()
        }
    }
}

@Composable
private fun MaisItemAtivo(subtitulo: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Brand.copy(alpha = 0.12f), RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.DonutLarge, contentDescription = null, tint = BrandActive, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text("Categorias e orçamentos", style = MaterialTheme.typography.bodyLarge, color = Ink)
                Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = Ink50)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Ink38)
        }
    }
}

@Composable
private fun MaisItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).background(Brand.copy(alpha = 0.12f), RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = BrandActive, modifier = Modifier.size(18.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = Ink,
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            )
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Ink38)
        }
    }
}

@Composable
private fun MaisItemFuturo(icon: ImageVector, label: String, badge: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).background(Ink.copy(alpha = 0.06f), RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Ink38, modifier = Modifier.size(18.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = Ink50,
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            )
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE6EEFB), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(badge, style = MaterialTheme.typography.labelSmall, color = Color(0xFF3B6DAA))
                }
            }
        }
    }
}

@Composable
private fun BankIconsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Hairline),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Ícones de banco prontos", style = MaterialTheme.typography.bodyLarge, color = Ink)
            Text(
                "Aguardando a feature de cartões de crédito",
                style = MaterialTheme.typography.bodySmall,
                color = Ink50,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                bankOptions.forEach { key -> BankBadge(key = key, size = 42) }
            }
        }
    }
}
