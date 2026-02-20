package com.example.poktreino.core.data.datainfopokemon

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_moves")
data class PokemonMoveEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pokemonId: Int,
    val nomeMove: String
)
