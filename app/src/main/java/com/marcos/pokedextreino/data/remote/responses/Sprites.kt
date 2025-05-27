package com.marcos.pokedextreino.data.remote.responses

data class Sprites(
    val back_default: String = "",
    val back_female: Any? = Any(),
    val back_shiny: String = "",
    val back_shiny_female: Any? = Any(),
    val front_default: String = "",
    val front_female: Any? = Any(),
    val front_shiny: String = "",
    val front_shiny_female: Any? = Any(),
    val other: Other = Other(),
    val versions: Versions = Versions()
)