package com.example.poktreino.core.data.domain.repository

import com.example.poktreino.core.data.dao.PokemonDao
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import com.example.poktreino.core.data.mapper.toEntity
import com.example.poktreino.core.data.mapper.toEvolutionEntities
import com.example.poktreino.core.data.mapper.toMovesEntities
import com.example.poktreino.core.data.mapper.toStatsEntities
import com.example.poktreino.core.data.remote.PokeApiService
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl(
    private val api: PokeApiService,
    private val dao: PokemonDao
) : PokemonRepository {


    override fun getPokemonList(): Flow<List<PokemonEntity>> = dao.getAllPokemons()


    override fun getPokemonWithDetails(id: Int): Flow<PokemonWithDetails?> =
        dao.getPokemonWithDetails(id)


    override suspend fun syncPokemonData(limit: Int, offset: Int): Result<Unit> {
        return try {

            val listResponse = api.getPokemonList(limit, offset)


            listResponse.results.forEach { resource ->
                val pokemonDto = api.getPokemonDetails(resource.name)


                val entity = pokemonDto.toEntity()
                val stats = pokemonDto.toStatsEntities()
                val moves = pokemonDto.toMovesEntities()
                val evolutions = pokemonDto.toEvolutionEntities()


                dao.insertFullPokemon(entity, stats, moves, evolutions)
            }
            Result.success(Unit)
        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}