package com.example.poktreino.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poktreino.core.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonUiState>(PokemonUiState.Loading)
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    // Controla a paginação baseada na quantidade de itens no banco
    private var currentOffset = 0
    private var isFetching = false

    init {
        observePokemonList()
        fetchPokemons()
    }

    private fun observePokemonList() {
        viewModelScope.launch {
            repository.getPokemonList()
                .catch { _uiState.value = PokemonUiState.Error("Erro ao acessar banco local") }
                .collect { list ->
                    // Emitir Success sempre evita o erro de "null object reference"
                    _uiState.value = PokemonUiState.Success(list)
                    // Atualiza o offset para a próxima busca baseando-se no tamanho atual da lista
                    currentOffset = list.size
                }
        }
    }

    fun fetchPokemons() {
        // Evita chamadas repetidas enquanto uma sincronização está ativa
        if (isFetching) return

        viewModelScope.launch {
            isFetching = true
            // Uso do Dispatchers.IO para operações de rede e banco de dados
            val result = withContext(Dispatchers.IO) {
                repository.syncPokemonData(limit = 20, offset = currentOffset)
            }

            result.onFailure { error ->
                val currentState = _uiState.value
                val isDataEmpty = currentState is PokemonUiState.Success && currentState.pokemons.isEmpty()

                if (currentState is PokemonUiState.Loading || isDataEmpty) {
                    _uiState.value = PokemonUiState.Error("Falha na sincronização: ${error.message}")
                }
            }
            isFetching = false
        }
    }
}