package com.example.poktreino.ui.viewmodel

import com.example.poktreino.core.data.datalocal.PokemonEntity

sealed interface PokemonUiState {
    object Loading : PokemonUiState
    data class Success(val pokemons: List<PokemonEntity>) : PokemonUiState
    data class Error(val message: String) : PokemonUiState
}