package com.example.poktreino.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.ui.screen.components.TypeBadge
import com.example.poktreino.ui.screen.components.getGradientForType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedCard(pokemon: PokemonEntity, onItemClick: (Int) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "Scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(getGradientForType(pokemon.tipoPrincipal))
            .clickable { onItemClick(pokemon.pokemonId) }
            .padding(24.dp)
    ) {
        Column(Modifier.align(Alignment.CenterStart).fillMaxHeight(), Arrangement.Center) {
            Text("Destaque do Dia", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelLarge)
            Text(pokemon.nome, color = Color.White, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))
            TypeBadge(type = pokemon.tipoPrincipal)
        }
        AsyncImage(
            model = pokemon.urlImagem,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterEnd)
                .graphicsLayer(scaleX = scale, scaleY = scale)
        )
    }
}