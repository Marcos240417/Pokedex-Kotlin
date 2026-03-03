package com.example.poktreino.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.poktreino.R
import com.example.poktreino.core.data.datalocal.PokemonEntity
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
    val pagingItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var selectedType by remember { mutableStateOf<String?>(null) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val isInitialLoading = pagingItems.loadState.refresh is LoadState.Loading
    val isNotLoading = pagingItems.loadState.refresh is LoadState.NotLoading
    val showButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    val animeBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF0A1221), Color(0xFF1B3D7B))
    )

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = { scope.launch { listState.animateScrollToItem(0) } },
                    containerColor = Color(0xFFE91E63),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Voltar ao topo")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(animeBackground).padding(padding)) {
            Column {
                SearchBarTop(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchChanged(it) },
                    onClearQuery = { viewModel.onSearchChanged("") }
                )

                PullToRefreshBox(
                    isRefreshing = isInitialLoading,
                    onRefresh = { pagingItems.refresh() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        // 1. CABEÇALHO COM KEYS FIXAS (Evita reset de scroll)
                        if (searchQuery.isEmpty()) {
                            item(key = "header_banner") {
                                BannerCarousel(imageIds = listOf(
                                    R.drawable.poke1, R.drawable.poke2, R.drawable.poke3, R.drawable.poke4
                                ))
                            }

                            item(key = "header_filters") {
                                TypeFilterBar(
                                    selectedType = selectedType,
                                    onTypeSelected = { selectedType = it }
                                )
                            }

                            if (pagingItems.itemCount > 0) {
                                item(key = "header_featured") {
                                    pagingItems[0]?.let { destaque ->
                                        FeaturedCard(
                                            pokemon = destaque,
                                            onItemClick = { onPokemonClick(it) }
                                        )
                                    }
                                }
                            }
                        }

                        // 2. LISTAGEM PRINCIPAL
                        when {
                            // Se a lista estiver vazia e carregando o primeiro lote
                            isInitialLoading && pagingItems.itemCount == 0 -> {
                                items(10, key = { "shimmer_$it" }) { PokemonItemPlaceholder() }
                            }

                            // Se terminou de carregar e não há nada no banco
                            isNotLoading && pagingItems.itemCount == 0 -> {
                                item(key = "empty_view") {
                                    EmptySearchResult(
                                        query = searchQuery.ifEmpty { "Pokémon" },
                                        onClearFilters = { viewModel.onSearchChanged("") }
                                    )
                                }
                            }

                            // LISTA COM DADOS E ‘SCROLL’ INFINITO
                            else -> {
                                items(
                                    count = pagingItems.itemCount,
                                    // pokemonId como key é vital para manter a posição do ‘scroll’
                                    key = pagingItems.itemKey { pokemon: PokemonEntity -> pokemon.pokemonId }
                                ) { index ->
                                    val pokemon = pagingItems[index]

                                    // GATILHO ESTABILIZADO: Sincroniza com o count real da lista
                                    LaunchedEffect(index) {
                                        if (index >= pagingItems.itemCount - 3 && searchQuery.isEmpty()) {
                                            viewModel.fetchMorePokemons(pagingItems.itemCount)
                                        }
                                    }

                                    pokemon?.let {
                                        val matchesType = selectedType == null ||
                                                it.tipoPrincipal.equals(selectedType, ignoreCase = true)

                                        if (matchesType) {
                                            // animateItem() remove o efeito de piscar
                                            Box(Modifier.animateItem()) {
                                                PokemonItem(
                                                    pokemon = it,
                                                    onItemClick = onPokemonClick
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 3. INDICADOR DE CARREGAMENTO NO RODAPÉ (PAGING APPEND)
                        if (pagingItems.loadState.append is LoadState.Loading) {
                            item(key = "loading_append") {
                                Box(Modifier.fillMaxWidth().padding(24.dp), Alignment.Center) {
                                    CircularProgressIndicator(
                                        color = Color(0xFFE91E63),
                                        strokeWidth = 2.dp
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
                        .clip(shape = RoundedCornerShape(16.dp)),
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
fun EmptySearchResult(
    query: String,
    onClearFilters: () -> Unit
) {
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
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onClearFilters,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Voltar para aba Todos", color = Color.White)
        }
    }
}