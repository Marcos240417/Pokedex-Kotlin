package com.marcos.pokedextreino.data.remote.responses

data class Ability(
    val ability: AbilityX = AbilityX(),
    val is_hidden: Boolean = false,
    val slot: Int = 0
)