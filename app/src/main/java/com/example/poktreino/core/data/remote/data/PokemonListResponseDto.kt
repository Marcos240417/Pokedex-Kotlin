package com.example.poktreino.core.data.remote.data

data class PokemonListResponseDto(
    val results: List<NamedResourceDto>
)
// Representa o dossiê completo do Pokémon (Nível 2)
data class NamedResourceDto(
    val name: String,
    val url: String
)
