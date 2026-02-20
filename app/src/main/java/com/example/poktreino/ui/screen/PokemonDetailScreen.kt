package com.example.poktreino.ui.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.poktreino.ui.screen.components.getGradientForType
import com.example.poktreino.ui.viewmodel.PokemonDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = koinViewModel()
) {
    val details by viewModel.detailState.collectAsState()
    LaunchedEffect(pokemonId) { viewModel.loadPokemonDetails(pokemonId) }

    Scaffold(
        containerColor = Color(0xFF121212) // Fundo escuro para destacar o gradiente
    ) { paddingValues ->
        details?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Cabeçalho Vibrante
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .background(getGradientForType(data.pokemon.tipoPrincipal)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = data.pokemon.urlImagem,
                            contentDescription = null,
                            modifier = Modifier.size(280.dp)
                        )
                        Text(
                            text = "#${data.pokemon.pokemonId.toString().padStart(3, '0')}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Conteúdo de Detalhes
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = data.pokemon.nome,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "ESTATÍSTICAS BASE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    data.stats.forEach { stat ->
                        StatRow(label = stat.nomeStat, value = stat.valorBase, maxTarget = 255f)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        } ?: Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun StatRow(label: String, value: Int, maxTarget: Float) {
    var animationStarted by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) value / maxTarget else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "StatAnimation"
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1.2f),
            style = MaterialTheme.typography.bodyMedium
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(2.5f)
                .height(10.dp),
            strokeCap = StrokeCap.Round,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = value.toString(),
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold
        )
    }
}