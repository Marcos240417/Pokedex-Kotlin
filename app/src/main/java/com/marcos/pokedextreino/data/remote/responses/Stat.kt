package com.marcos.pokedextreino.data.remote.responses

data class Stat(
    val base_stat: Int = 0,
    val effort: Int = 0,
    val stat: StatX = StatX()
)