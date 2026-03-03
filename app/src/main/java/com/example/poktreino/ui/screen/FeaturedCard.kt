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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.ui.screen.components.TypeBadge
import com.example.poktreino.ui.screen.components.getGradientForType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedCard(pokemon: PokemonEntity, onItemClick: (Int) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(2500, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "Scale"
    )

    // Gradiente sutil baseado no tipo para o efeito de vidro
    val typeColor = getGradientForType(pokemon.tipoPrincipal)
    val glassGradient = Brush.linearGradient(
        colors = listOf(
            typeColor.copy(alpha = 0.4f),
            typeColor.copy(alpha = 0.15f)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Altura ajustada para elegância
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(pokemon.pokemonId) },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.1f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(glassGradient) // Efeito translúcido
        ) {
            // Decoração de fundo (Pokebola sutil)
            Icon(
                imageVector = Icons.Default.CatchingPokemon,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 40.dp, y = 20.dp)
                    .graphicsLayer(alpha = 0.05f),
                tint = Color.White
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 28.dp)
            ) {
                Surface(
                    color = Color.White.copy(0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "DESTAQUE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = pokemon.nome,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(12.dp))

                TypeBadge(type = pokemon.tipoPrincipal)
            }

            AsyncImage(
                model = pokemon.urlImagem,
                contentDescription = pokemon.nome,
                modifier = Modifier
                    .size(170.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationY = -10f // Leve flutuação
                    )
            )
        }
    }
}