package com.example.poktreino.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PokemonViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val mutex = Mutex()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pokemonPagingData: Flow<PagingData<PokemonEntity>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            repository.getPagedPokemons(query)
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    init {
        fetchMorePokemons(0)
    }

    fun fetchMorePokemons(currentCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (mutex.isLocked) return@launch

            mutex.withLock {
                // Usa o tamanho real da lista como offset para evitar duplicados
                repository.syncPokemonData(limit = 20, offset = currentCount)
            }
        }
    }

    fun onSearchChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}