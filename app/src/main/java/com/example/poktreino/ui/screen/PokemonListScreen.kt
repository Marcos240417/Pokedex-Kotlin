package com.example.poktreino.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.poktreino.R
import com.example.poktreino.ui.viewmodel.PokemonUiState
import com.example.poktreino.ui.viewmodel.PokemonViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = koinViewModel(),
    onPokemonClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Filtro de busca por nome ou ID (Ex: "Bulbasaur" ou "1")
    val filteredPokemons = remember(state, searchQuery) {
        if (state is PokemonUiState.Success) {
            val list = (state as PokemonUiState.Success).pokemons
            if (searchQuery.isEmpty()) list
            else list.filter {
                it.nome.contains(searchQuery, ignoreCase = true) ||
                        it.pokemonId.toString() == searchQuery
            }
        } else emptyList()
    }

    // Lista estática de IDs de recursos para evitar avisos de performance
    val banners = remember {
        listOf(
            R.drawable.poke1, R.drawable.poke2, R.drawable.poke3,
            R.drawable.poke4, R.drawable.poke5, R.drawable.poke6,
            R.drawable.poke7, R.drawable.poke8, R.drawable.poke9,
            R.drawable.poke10, R.drawable.poke11, R.drawable.poke12
        )
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5) // Fundo cinza claro para destacar os cards
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            // 1. BARRA DE BUSCA (Top-level, sem tarja preta)
            SearchBarTop(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.fillMaxSize()
            ) {
                // 2. CARROSSEL DE IMAGENS (Auto-scroll + Dots)
                item {
                    BannerCarousel(imageIds = banners)
                }

                // 3. LISTA DE POKÉMONS (Sem título "Todos os Pokémons")
                items(items = filteredPokemons, key = { it.pokemonId }) { pokemon ->
                    PokemonItem(pokemon = pokemon, onItemClick = onPokemonClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarTop(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Search by name or number...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp),
            singleLine = true
        )

        // Botão de busca com degradê roxo/rosa (Estilo De luxo)
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF9C27B0), Color(0xFFE91E63))),
                    shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun BannerCarousel(imageIds: List<Int>) {
    val pagerState = rememberPagerState(pageCount = { imageIds.size })

    // Movimentação automática a cada 4 segundos
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            if (pagerState.pageCount > 0) {
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { page ->
            Image(
                painter = painterResource(id = imageIds[page]),
                contentDescription = "Promoção",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillBounds // Preenche o card conforme solicitado
            )
        }

        // Indicadores de Página (Dots)
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(imageIds.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }

                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}