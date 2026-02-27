package com.example.poktreino.ui.viewmodel

sealed interface PokemonUiState<out T> {
    object Loading : PokemonUiState<Nothing>
    data class Success<T>(val data: T) : PokemonUiState<T>
    data class Error(val message: String) : PokemonUiState<Nothing>
}