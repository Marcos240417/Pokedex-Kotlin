package com.example.poktreino.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.poktreino.core.data.datainfopokemon.PokemonEvolutionEntity

@Composable
fun EvolutionCard(
    evolution: PokemonEvolutionEntity,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Círculo de fundo brilhante
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    .graphicsLayer { shadowElevation = 10f }
            )
            AsyncImage(
                model = evolution.urlImagem,
                contentDescription = evolution.nomeEvolucao,
                modifier = Modifier.size(85.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = evolution.nomeEvolucao.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}