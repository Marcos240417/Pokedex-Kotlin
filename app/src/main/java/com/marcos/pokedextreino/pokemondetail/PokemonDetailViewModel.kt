package com.marcos.pokedextreino.pokemondetail

import androidx.lifecycle.ViewModel
import com.marcos.pokedextreino.data.remote.responses.Pokemon
import com.marcos.pokedextreino.repository.PokemonRepository
import com.marcos.pokedextreino.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) :
    ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon>{
        return repository.getPokemonInfo(pokemonName)
    }
}