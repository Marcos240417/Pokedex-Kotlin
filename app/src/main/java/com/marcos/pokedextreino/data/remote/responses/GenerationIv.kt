package com.marcos.pokedextreino.data.remote.responses

data class GenerationIv(
    val diamond_pearl: DiamondPearl = DiamondPearl(),
    val heartgold_soulsilver: HeartgoldSoulsilver = HeartgoldSoulsilver(),
    val platinum: Platinum = Platinum()
)