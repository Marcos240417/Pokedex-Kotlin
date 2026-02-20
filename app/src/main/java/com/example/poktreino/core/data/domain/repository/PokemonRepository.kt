package com.example.poktreino.core.domain.repository

import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.datalocal.PokemonWithDetails
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    /**
     * Retorna um fluxo reativo com a lista de todos os Pokémons salvos localmente.
     * Usado para popular a tela principal (Home).
     */
    fun getPokemonList(): Flow<List<PokemonEntity>>

    /**
     * Retorna os detalhes completos de um único Pokémon (Stats, Moves, Evolutions).
     * @param id O identificador único do Pokémon.
     */
    fun getPokemonWithDetails(id: Int): Flow<PokemonWithDetails?>

    /**
     * Sincroniza os dados da API com o banco de dados local.
     * Busca a lista e, para cada item, baixa os detalhes e salva no Room.
     */
    suspend fun syncPokemonData(limit: Int, offset: Int): Result<Unit>
}