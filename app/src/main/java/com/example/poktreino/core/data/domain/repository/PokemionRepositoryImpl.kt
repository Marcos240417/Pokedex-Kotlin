package com.example.poktreino.core.data.repository

import com.example.poktreino.core.data.dao.PokemonDao
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import com.example.poktreino.core.data.mapper.*
import com.example.poktreino.core.data.remote.PokeApiService
import com.example.poktreino.core.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class PokemonRepositoryImpl(
    private val api: PokeApiService,
    private val dao: PokemonDao
) : PokemonRepository {

    // 1. Busca a lista simples do banco (UI reativa)
    override fun getPokemonList(): Flow<List<PokemonEntity>> = dao.getAllPokemons()

    // 2. Busca o Pokémon completo com todos os detalhes (@Relation)
    override fun getPokemonWithDetails(id: Int): Flow<PokemonWithDetails?> =
        dao.getPokemonWithDetails(id)

    /**
     * 3. Sincronização: A "Mágica" do Repositório
     */
    override suspend fun syncPokemonData(limit: Int, offset: Int): Result<Unit> {
        return try {
            // A. Busca a lista inicial (nomes e URLs)
            val listResponse = api.getPokemonList(limit, offset)

            // B. Para cada item, buscamos o detalhe completo
            listResponse.results.forEach { resource ->
                val pokemonDto = api.getPokemonDetails(resource.name)

                // C. Usamos seus Mappers para converter em Entities
                val entity = pokemonDto.toEntity()
                val stats = pokemonDto.toStatsEntities()
                val moves = pokemonDto.toMovesEntities()
                val evolutions = pokemonDto.toEvolutionEntities()

                // D. Salvamos tudo no banco em uma única transação segura
                dao.insertFullPokemon(entity, stats, moves, evolutions)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            // Tratamento de erro (falta de internet, servidor fora, etc)
            Result.failure(e)
        }
    }
}