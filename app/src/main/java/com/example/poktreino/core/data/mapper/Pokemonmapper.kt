package com.example.poktreino.core.data.mapper

import com.example.poktreino.core.data.datainfopokemon.*
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.remote.data.PokemonDto

fun PokemonDto.toEntity(): PokemonEntity {
    return PokemonEntity(
        pokemonId = this.id,
        nome = this.name.replaceFirstChar { it.uppercase() },
        altura = this.height,
        peso = this.weight,
        urlImagem = this.sprites.imageUrl,
        tipoPrincipal = this.types?.getOrNull(0)?.type?.name?.replaceFirstChar { it.uppercase() } ?: "Normal",
        tipoSecundario = this.types?.getOrNull(1)?.type?.name?.replaceFirstChar { it.uppercase() }
    )
}

fun PokemonDto.toStatsEntities(): List<PokemonStatEntity> {
    return this.stats?.map { statDto ->
        PokemonStatEntity(
            pokemonId = this.id,
            nomeStat = statDto.stat.name.replaceFirstChar { it.uppercase() },
            valorBase = statDto.basestat
        )
    } ?: emptyList()
}

fun PokemonDto.toMovesEntities(): List<PokemonMoveEntity> {
    return this.moves?.map { moveDto ->
        PokemonMoveEntity(
            pokemonId = this.id,
            nomeMove = moveDto.move.name
                .replace("-", " ")
                .replaceFirstChar { it.uppercase() }
        )
    } ?: emptyList()
}

fun PokemonDto.toEvolutionEntities(): List<PokemonEvolutionEntity> {
    val evolutions = mutableListOf<PokemonEvolutionEntity>()

    this.evo?.forEach { chain ->
        chain.evolution.forEach { namedResource ->
            val extractedId = namedResource.url
                .trimEnd('/')
                .substringAfterLast("/")
                .toIntOrNull() ?: return@forEach

            if (extractedId != this.id) {
                evolutions.add(
                    PokemonEvolutionEntity(
                        pokemonId = this.id,
                        pokemonEvolucaoId = extractedId,
                        nomeEvolucao = namedResource.name.replaceFirstChar { it.uppercase() }
                    )
                )
            }
        }
    }
    return evolutions
}