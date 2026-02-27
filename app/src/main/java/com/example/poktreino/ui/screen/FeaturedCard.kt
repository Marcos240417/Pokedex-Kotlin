package com.example.poktreino.ui.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
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
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "Scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(pokemon.pokemonId) },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getGradientForType(pokemon.tipoPrincipal))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
            ) {
                Text(
                    "DESTAQUE",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    pokemon.nome,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(8.dp))
                TypeBadge(type = pokemon.tipoPrincipal)
            }

            AsyncImage(
                model = pokemon.urlImagem,
                contentDescription = pokemon.nome,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeaturedCardPreview() {
    val samplePokemon = PokemonEntity(
        pokemonId = 4,
        nome = "Charmander",
        altura = 6,
        peso = 85,
        urlImagem = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
        tipoPrincipal = "fire",
        tipoSecundario = null
    )
    FeaturedCard(pokemon = samplePokemon, onItemClick = {})
}
