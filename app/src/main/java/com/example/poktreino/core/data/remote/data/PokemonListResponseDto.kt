package com.example.poktreino.core.data.remote.data

data class PokemonListResponseDto(
    val results: List<NamedResourceDto>
)

data class NamedResourceDto(
    val name: String,
    val url: String
)
