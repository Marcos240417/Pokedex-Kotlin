package com.example.poktreino.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color // IMPORT ESSENCIAL PARA O .copy()
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.poktreino.R
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.ui.screen.components.TypeBadge
import com.example.poktreino.ui.screen.components.getGradientForType

@Composable
fun PokemonItem(pokemon: PokemonEntity, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(190.dp) // Aumentado levemente para garantir respiro vertical
            .clickable { onItemClick(pokemon.pokemonId) },
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        val typeColor = getGradientForType(pokemon.tipoPrincipal)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            typeColor.copy(alpha = 0.9f),
                            Color(0xFF1E1E2E)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ÁREA DA IMAGEM COM AURA REFORÇADA
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(130.dp)) {
                    // Círculo com gradiente radial para efeito de profundidade
                    Surface(
                        modifier = Modifier.size(115.dp),
                        color = Color.Transparent,
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.background(
                                Brush.radialGradient(
                                    colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent)
                                )
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PokemonImage(
                            pokemonId = pokemon.pokemonId,
                            pokemonNome = pokemon.nome,
                            modifier = Modifier.size(110.dp)
                        )

                        // CORREÇÃO DO ID CORTADO
                        Text(
                            text = "#${pokemon.pokemonId.toString().padStart(3, '0')}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                lineHeight = 20.sp, // Garante que o caractere não seja ceifado no topo
                                letterSpacing = 1.sp
                            ),
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(top = 4.dp) // Adiciona respiro para evitar o corte
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // ÁREA DE INFORMAÇÕES (Mantida conforme sua lógica)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pokemon.nome.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )

                    Row(modifier = Modifier.padding(vertical = 10.dp)) {
                        TypeBadge(type = pokemon.tipoPrincipal)
                        pokemon.tipoSecundario?.let {
                            Spacer(Modifier.width(8.dp))
                            TypeBadge(type = it)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        InfoItem(Icons.Default.MonitorWeight, "${pokemon.peso / 10.0} kg", "Peso")
                        Spacer(Modifier.width(24.dp))
                        InfoItem(Icons.Default.Height, "${pokemon.altura / 10.0} m", "Altura")
                    }
                }
            }
        }
    }
}
@Composable
fun PokemonImage(
    pokemonId: Int,
    pokemonNome: String,
    modifier: Modifier = Modifier
) {
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = pokemonNome,
        placeholder = painterResource(id = R.drawable.pokeball_placeholder),
        error = painterResource(id = R.drawable.pokeball_placeholder),
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

@Composable
fun InfoItem(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color.White.copy(alpha = 0.8f)
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}