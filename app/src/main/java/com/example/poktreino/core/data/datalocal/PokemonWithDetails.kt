package com.example.poktreino.core.data.datalocal

import androidx.room.Embedded
import androidx.room.Relation
import com.example.poktreino.core.data.datainfopokemon.PokemonEvolutionEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonMoveEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonStatEntity

data class PokemonWithDetails(
    @Embedded
    val pokemon: PokemonEntity,

    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId"
    )
    val stats: List<PokemonStatEntity>,

    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId"
    )
    val moves: List<PokemonMoveEntity>,

    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId"
    )
    val evolutions: List<PokemonEvolutionEntity>
)