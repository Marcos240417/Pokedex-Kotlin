package com.example.poktreino.core.data.remote.data

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<Type>?,
    val moves: List<Move>?,
    val stats: List<Stat>?,
    val evo: List<EvolutionChain>? // nullable
)

data class Sprites(
    @SerializedName("front_default")
    val imageUrl: String
)

data class Type(
    val type: NamedResourceDto
)

data class Stat(
    @SerializedName("base_stat")
    val basestat: Int,
    val stat: NamedResourceDto
)

data class Move(
    val move: NamedResourceDto
)

data class EvolutionChain(
    @SerializedName("pokemon_evolutions")
    val evolution: List<NamedResourceDto>
)




