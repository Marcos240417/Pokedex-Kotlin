package com.marcos.pokedextreino.data.remote.responses

data class PastAbility(
    val abilities: List<AbilityXX> = listOf(),
    val generation: Generation = Generation()
)