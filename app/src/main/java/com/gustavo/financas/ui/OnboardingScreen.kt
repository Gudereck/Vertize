package com.gustavo.financas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gustavo.financas.ui.theme.Brand
import com.gustavo.financas.ui.theme.Ink
import com.gustavo.financas.ui.theme.Ink50
import com.gustavo.financas.ui.theme.Ink70
import com.gustavo.financas.ui.theme.Surface

@Composable
fun OnboardingScreen(
    onAddFirstTransaction: () -> Unit,
    onExploreWithSampleData: () -> Unit
) {
    Scaffold(containerColor = Surface) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(start = 30.dp, top = 26.dp, end = 30.dp, bottom = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Brand, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(45f)
                        .background(Color.White, RoundedCornerShape(4.dp))
                )
            }
            Text(
                text = "Bem-vindo ao\nVertize",
                style = MaterialTheme.typography.displaySmall,
                color = Ink,
                modifier = Modifier.padding(top = 18.dp)
            )
            Text(
                text = "Organize suas finanças, defina metas e acompanhe seus gastos por categoria, tudo em um só lugar.",
                style = MaterialTheme.typography.bodyMedium,
                color = Ink70,
                modifier = Modifier.padding(top = 10.dp)
            )

            Spacer(Modifier.height(26.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(212.dp)
                    .background(Color(0xFFF7F7F8), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "ilustração de onboarding",
                    style = MaterialTheme.typography.bodySmall,
                    color = Ink50
                )
            }

            Spacer(Modifier.height(26.dp))
            Bullet(numero = 1, texto = "Lance receitas e despesas em segundos, com categorias já prontas.")
            Spacer(Modifier.height(14.dp))
            Bullet(numero = 2, texto = "Defina um limite mensal por categoria e receba aviso quando estourar.")
            Spacer(Modifier.height(14.dp))
            Bullet(numero = 3, texto = "Crie metas de economia e acompanhe o quanto falta para chegar lá.")

            Spacer(Modifier.weight(1f))
            Button(
                onClick = onAddFirstTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(17.dp)
            ) {
                Text("Adicionar primeiro lançamento", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            }
            TextButton(
                onClick = onExploreWithSampleData,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Explorar com dados de exemplo", style = MaterialTheme.typography.bodyMedium, color = Ink50)
            }
        }
    }
}

@Composable
private fun Bullet(numero: Int, texto: String) {
    Row {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(Brand.copy(alpha = 0.14f), RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(numero.toString(), style = MaterialTheme.typography.labelLarge, color = Brand)
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = Ink70,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
