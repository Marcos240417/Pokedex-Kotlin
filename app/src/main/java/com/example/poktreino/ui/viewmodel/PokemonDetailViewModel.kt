package com.example.poktreino.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import com.example.poktreino.core.data.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<PokemonUiState<PokemonWithDetails>>(PokemonUiState.Loading)
    val uiState: StateFlow<PokemonUiState<PokemonWithDetails>> = _uiState.asStateFlow()

    fun loadPokemonDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = PokemonUiState.Loading
            repository.getPokemonWithDetails(id)
                .catch { e -> _uiState.value = PokemonUiState.Error(e.message ?: "Erro") }
                .collect { details ->
                    _uiState.value = if (details != null) PokemonUiState.Success(details)
                    else PokemonUiState.Error("Pokémon não encontrado")
                }
        }
    }
}