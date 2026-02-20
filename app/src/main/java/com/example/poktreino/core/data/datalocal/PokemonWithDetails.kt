package com.example.poktreino.core.data.datalocal

import androidx.room.Embedded
import androidx.room.Relation
import com.example.poktreino.core.data.datainfopokemon.PokemonEvolutionEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonMoveEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonStatEntity

/**
 * Esta classe não é uma tabela, mas sim um "pacote" que une as 4 tabelas.
 */
data class PokemonWithDetails(
    // 1. O Objeto Pai (os dados básicos do Pokémon)
    @Embedded
    val pokemon: PokemonEntity,

    // 2. Busca todos os Status que possuem o mesmo 'pokemonId'
    @Relation(
        parentColumn = "pokemonId", // Nome da coluna na PokemonEntity
        entityColumn = "pokemonId"  // Nome da coluna na PokemonStatEntity
    )
    val stats: List<PokemonStatEntity>,

    // 3. Busca todos os Movimentos
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId"
    )
    val moves: List<PokemonMoveEntity>,

    // 4. Busca todas as Evoluções
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId"
    )
    val evolutions: List<PokemonEvolutionEntity>
)