package com.marcos.pokedextreino.data.remote.responses

data class Other(
    val dream_world: DreamWorld = DreamWorld(),
    val home: Home = Home(),
    val official_artwork: OfficialArtwork = OfficialArtwork(),
    val showdown: Showdown = Showdown()
)