package com.example.poktreino.core.data.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.poktreino.core.data.dao.PokemonDao
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import com.example.poktreino.core.data.remote.PokeApiService
import com.example.poktreino.core.data.mapper.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PokemonRepositoryImpl(
    private val api: PokeApiService,
    private val dao: PokemonDao
) : PokemonRepository {

    override fun getPokemonList(): Flow<List<PokemonEntity>> = dao.getAllPokemons()

    override fun getPokemonWithDetails(id: Int): Flow<PokemonWithDetails?> = dao.getPokemonWithDetails(id)

    override suspend fun syncPokemonData(limit: Int, offset: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val listResponse = api.getPokemonList(limit, offset)
                listResponse.results.forEach { resource ->
                    val pokemonDto = api.getPokemonDetails(resource.name)
                    dao.insertFullPokemon(
                        pokemon = pokemonDto.toEntity(),
                        stats = pokemonDto.toStatsEntities(),
                        moves = pokemonDto.toMovesEntities(),
                        evolutions = pokemonDto.toEvolutionEntities()
                    )
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun getPagedPokemons(query: String): Flow<PagingData<PokemonEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                enablePlaceholders = true, // ESSENCIAL: Mantém o scroll estável
                initialLoadSize = 40       // Carrega mais no início para evitar saltos
            ),
            pagingSourceFactory = { dao.getPokemonPagingSource(query) }
        ).flow
    }
}