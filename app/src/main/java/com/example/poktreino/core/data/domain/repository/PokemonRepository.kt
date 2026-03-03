package com.example.poktreino.core.data.domain.repository

import androidx.paging.PagingData
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<PokemonEntity>>
    fun getPokemonWithDetails(id: Int): Flow<PokemonWithDetails?>
    suspend fun syncPokemonData(limit: Int, offset: Int): Result<Unit>
    // Agora com suporte a query para Paging 3
    fun getPagedPokemons(query: String): Flow<PagingData<PokemonEntity>>
}