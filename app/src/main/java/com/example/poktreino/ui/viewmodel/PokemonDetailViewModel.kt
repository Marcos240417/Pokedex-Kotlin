package com.example.poktreino.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import com.example.poktreino.core.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _detailState = MutableStateFlow<PokemonWithDetails?>(null)
    val detailState: StateFlow<PokemonWithDetails?> = _detailState.asStateFlow()

    fun loadPokemonDetails(id: Int) {
        viewModelScope.launch (Dispatchers.IO){
            repository.getPokemonWithDetails(id).collect { details ->
                _detailState.value = details
            }
        }
    }
}