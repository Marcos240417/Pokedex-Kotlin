package com.marcos.pokedextreino.repository

import com.marcos.pokedextreino.data.remote.PokeApi
import com.marcos.pokedextreino.data.remote.responses.Pokemon
import com.marcos.pokedextreino.data.remote.responses.PokemonList
import com.marcos.pokedextreino.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(

    private val api: PokeApi
){
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        return try {
            val response = api.getPokemonList(limit, offset)
            Resource.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Erro ao carregar lista de Pokémon")  // Log do erro
            Resource.Error("Erro ao carregar Pokémon: ${e.localizedMessage}")
        }
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return try {
            val response = api.getPokemonInfo(pokemonName)
            Resource.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Erro ao carregar informações do Pokémon $pokemonName")  // Log do erro
            Resource.Error("Erro ao carregar informações: ${e.localizedMessage}")
        }
    }

}