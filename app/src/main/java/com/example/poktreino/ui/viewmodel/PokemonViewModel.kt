package com.example.poktreino.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PokemonUiState<List<PokemonEntity>>>(PokemonUiState.Loading)
    val uiState: StateFlow<PokemonUiState<List<PokemonEntity>>> = _uiState.asStateFlow()

    private var currentOffset = 0
    private var isFetching = false
    private var canLoadMore = true

    init {
        observePokemonList()
    }

    private fun observePokemonList() {
        viewModelScope.launch (Dispatchers.IO){
            repository.getPokemonList()
                .catch { _uiState.value = PokemonUiState.Error("Erro ao acessar banco local") }
                .collect { list ->
                    if (list.isNotEmpty() || !isFetching) {
                        _uiState.value = PokemonUiState.Success(list)
                        currentOffset = list.size
                    }

                    if (list.isEmpty() && !isFetching && canLoadMore) {
                        fetchPokemons()
                    }
                }
        }
    }

    fun fetchPokemons() {
        if (isFetching || !canLoadMore) return

        viewModelScope.launch (Dispatchers.IO){
            isFetching = true
            val result = withContext(Dispatchers.IO) {
                repository.syncPokemonData(limit = 20, offset = currentOffset)
            }

            result.onSuccess {
            }.onFailure { error ->
                if (_uiState.value is PokemonUiState.Loading) {
                    _uiState.value = PokemonUiState.Error("Falha na sincronização: ${error.message}")
                }
            }
            isFetching = false
        }
    }
}