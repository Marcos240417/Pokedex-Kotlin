package com.example.poktreino.core.data.datalocal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey
    val pokemonId: Int,
    val nome: String,
    val altura: Int,
    val peso: Int,
    val urlImagem: String,
    val tipoPrincipal: String,
    val tipoSecundario: String?
)