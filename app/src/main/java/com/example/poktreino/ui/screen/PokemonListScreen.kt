package com.example.poktreino.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.poktreino.R
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.ui.viewmodel.PokemonUiState
import com.example.poktreino.ui.viewmodel.PokemonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = koinViewModel(),
    onPokemonClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val showBackToTopButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= totalItems - 5 && totalItems > 0
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && searchQuery.isEmpty() && state is PokemonUiState.Success) {
            viewModel.fetchPokemons()
        }
    }

    val filteredPokemons = remember(state, searchQuery) {
        val currentState = state
        if (currentState is PokemonUiState.Success<List<PokemonEntity>>) {
            val list = currentState.data
            if (searchQuery.isEmpty()) list
            else list.filter {
                it.nome.contains(searchQuery, ignoreCase = true) ||
                        it.pokemonId.toString() == searchQuery
            }
        } else {
            emptyList()
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E1E2E), Color(0xFF11111B))
    )

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            AnimatedVisibility(
                visible = showBackToTopButton,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        coroutineScope.launch { listState.animateScrollToItem(0) }
                    },
                    containerColor = Color(0xFFE91E63),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Voltar ao topo")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(padding)
        ) {
            Column {
                SearchBarTop(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClearQuery = { searchQuery = "" }
                )

                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        coroutineScope.launch {
                            isRefreshing = true
                            viewModel.fetchPokemons()
                            delay(1000)
                            isRefreshing = false
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (val currentState = state) {
                        is PokemonUiState.Loading -> {
                            Box(Modifier.fillMaxSize(), Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFFE91E63))
                            }
                        }
                        is PokemonUiState.Error -> {
                            Box(Modifier.fillMaxSize(), Alignment.Center) {
                                Text(currentState.message, color = Color.White)
                            }
                        }
                        is PokemonUiState.Success -> {
                            if (filteredPokemons.isEmpty() && searchQuery.isNotEmpty()) {
                                EmptySearchResult(searchQuery)
                            } else {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    // CORREÇÃO: Espaçamento aumentado para 16.dp entre cards maiores
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    if (searchQuery.isEmpty()) {
                                        item {
                                            BannerCarousel(
                                                imageIds = listOf(
                                                    R.drawable.poke1, R.drawable.poke2, R.drawable.poke3,
                                                    R.drawable.poke4, R.drawable.poke5, R.drawable.poke6,
                                                    R.drawable.poke7, R.drawable.poke8, R.drawable.poke9,
                                                    R.drawable.poke10, R.drawable.poke11, R.drawable.poke12
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }

                                    items(
                                        items = filteredPokemons,
                                        key = { it.pokemonId }
                                    ) { pokemon ->
                                        PokemonItem(
                                            pokemon = pokemon,
                                            onItemClick = onPokemonClick
                                        )
                                    }

                                    if (searchQuery.isEmpty() && filteredPokemons.isNotEmpty()) {
                                        item {
                                            Box(
                                                Modifier.fillMaxWidth().padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(32.dp),
                                                    color = Color(0xFFE91E63)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BannerCarousel(imageIds: List<Int>) {
    val pagerState = rememberPagerState(pageCount = { imageIds.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            if (pagerState.pageCount > 0) {
                val next = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(next)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            pageSpacing = 12.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E2E)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageIds[page]),
                    contentDescription = "Banner",
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.FillBounds
                )
            }
        }


        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            Arrangement.Center
        ) {
            repeat(imageIds.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.4f))
                        .size(if (isSelected) 8.dp else 6.dp)
                )
            }
        }
    }
}

@Composable
fun SearchBarTop(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = {
            Text("Nome ou número do Pokémon...",
                color = Color.White.copy(0.5f))
        },
        leadingIcon = {
            Icon(Icons.Default.Search, null,
                tint = Color.White.copy(0.7f))
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(Icons.Default.Clear, null, tint = Color.White)
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(0.12f),
            unfocusedContainerColor = Color.White.copy(0.08f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color(0xFFE91E63)
        ),
        shape = RoundedCornerShape(28.dp),
        singleLine = true
    )
}

@Composable
fun EmptySearchResult(query: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nenhum Pokémon encontrado para:", color = Color.Gray)
        Text(
            "\"$query\"",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}