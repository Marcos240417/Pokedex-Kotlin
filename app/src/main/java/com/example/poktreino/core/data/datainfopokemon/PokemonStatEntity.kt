package com.example.poktreino.core.data.datainfopokemon

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_stats")
data class PokemonStatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pokemonId: Int,
    val nomeStat: String,
    val valorBase: Int
)