package com.marcos.pokedextreino.data.remote.responses

data class PokemonList(
    val count: Int = 0,
    val next: String = "",
    val previous: Any? = null,
    val results: List<Result> = listOf()
)