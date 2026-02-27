package com.example.poktreino.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.poktreino.core.data.datainfopokemon.PokemonEvolutionEntity

@Composable
fun EvolutionCard(
    evolution: PokemonEvolutionEntity,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(110.dp)
            .height(145.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        color = Color.White.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = "#${evolution.pokemonEvolucaoId.toString().padStart(3, '0')}",
                color = Color.White.copy(alpha = 0.4f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )

            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${evolution.pokemonEvolucaoId}.png",
                contentDescription = evolution.nomeEvolucao,
                modifier = Modifier.size(75.dp)
            )

            Text(
                text = evolution.nomeEvolucao,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}