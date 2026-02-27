package com.example.poktreino.core.data.domain.repository

import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(): Flow<List<PokemonEntity>>

    fun getPokemonWithDetails(id: Int): Flow<PokemonWithDetails?>

    suspend fun syncPokemonData(limit: Int, offset: Int): Result<Unit>
}