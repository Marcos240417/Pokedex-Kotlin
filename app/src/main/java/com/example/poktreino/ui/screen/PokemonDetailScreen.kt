package com.example.poktreino.ui.screen

import com.example.poktreino.ui.viewmodel.PokemonDetailViewModel
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.poktreino.core.data.datainfopokemon.PokemonEvolutionEntity
import com.example.poktreino.ui.screen.components.TypeBadge
import com.example.poktreino.ui.screen.components.getGradientForType
import com.example.poktreino.ui.viewmodel.PokemonUiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onEvolutionClick: (Int) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetails(pokemonId)
    }


    when (val state = uiState) {
        is PokemonUiState.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        is PokemonUiState.Error -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(text = state.message, color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }

        is PokemonUiState.Success -> {

            val details = state.data
            val pokemon = details.pokemon

            val primaryTypeColor = remember(pokemon.tipoPrincipal) {
                when (pokemon.tipoPrincipal.lowercase()) {
                    "grass" -> Color(0xFF78C850)
                    "fire" -> Color(0xFFF08030)
                    "water" -> Color(0xFF6890F0)
                    "electric" -> Color(0xFFF8D030)
                    "poison" -> Color(0xFFA040A0)
                    else -> Color.White
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(getGradientForType(pokemon.tipoPrincipal))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Toolbar Customizada
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                        Text(
                            text = "#${pokemon.pokemonId.toString().padStart(3, '0')}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val infiniteTransition = rememberInfiniteTransition(label = "Glow")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.4f,
                            targetValue = 0.8f,
                            animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
                            label = ""
                        )

                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .graphicsLayer(alpha = alpha)
                                .background(
                                    brush = Brush.radialGradient(
                                        listOf(
                                            primaryTypeColor,
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                        AsyncImage(
                            model = pokemon.urlImagem,
                            contentDescription = pokemon.nome,
                            modifier = Modifier.size(260.dp)
                        )
                    }


                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color.White.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 28.dp, vertical = 32.dp)) {
                            Text(
                                text = pokemon.nome,
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )

                            Row(modifier = Modifier.padding(vertical = 12.dp)) {
                                TypeBadge(type = pokemon.tipoPrincipal)
                                pokemon.tipoSecundario?.let {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    TypeBadge(type = it)
                                }
                            }

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                Arrangement.SpaceAround
                            ) {
                                DetailInfoItem(label = "PESO", value = "${pokemon.peso / 10.0} kg")
                                DetailInfoItem(
                                    label = "ALTURA",
                                    value = "${pokemon.altura / 10.0} m"
                                )
                            }

                            Text(
                                "ESTATÍSTICAS BASE",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Lista de Status do Banco
                            details.stats.distinctBy { it.nomeStat }.forEach { stat ->
                                StatRow(
                                    label = stat.nomeStat,
                                    value = stat.valorBase,
                                    maxTarget = 255f
                                )
                            }


                            if (details.evolutions.isNotEmpty()) {
                                EvolutionSection(
                                    evolutions = details.evolutions,
                                    onEvolutionClick = onEvolutionClick
                                )
                            }
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EvolutionSection(evolutions: List<PokemonEvolutionEntity>, onEvolutionClick: (Int) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 40.dp)) {
        Text(
            "EVOLUÇÕES",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(items = evolutions, key = { it.pokemonEvolucaoId }) { evolution ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EvolutionCard(
                        evolution = evolution,
                        onClick = { onEvolutionClick(evolution.pokemonEvolucaoId) })
                    if (evolution != evolutions.last()) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            null,
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun StatRow(label: String, value: Int, maxTarget: Float) {
    val statColor = remember(label) {
        when (label.uppercase()) {
            "HP" -> Color(0xFFFF0000)
            "ATTACK" -> Color(0xFFF08030)
            "DEFENSE" -> Color(0xFFF8D030)
            "SPECIAL-ATTACK" -> Color(0xFF6890F0)
            "SPECIAL-DEFENSE" -> Color(0xFF78C850)
            "SPEED" -> Color(0xFFF85888)
            else -> Color(0xFF4CAF50)
        }
    }

    var animationStarted by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) value / maxTarget else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = ""
    )

    LaunchedEffect(Unit) { animationStarted = true }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label.uppercase(),
            modifier = Modifier.weight(2.5f),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(4f)
                .height(10.dp)
                .clip(CircleShape),
            strokeCap = StrokeCap.Round,

            color = statColor,
            trackColor = Color.White.copy(alpha = 0.1f)
        )

        Text(
            text = value.toString().padStart(3, '0'),
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Black
        )
    }
}


@Composable
fun DetailInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text(
            label,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}