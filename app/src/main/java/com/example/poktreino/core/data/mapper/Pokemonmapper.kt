package com.example.poktreino.core.data.mapper

import com.example.poktreino.core.data.datainfopokemon.*
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.remote.data.PokemonDto

// 1. Dados Básicos
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

// 2. Status
fun PokemonDto.toStatsEntities(): List<PokemonStatEntity> {
    return this.stats?.map { statDto ->
        PokemonStatEntity(
            pokemonId = this.id,
            nomeStat = statDto.stat.name.replaceFirstChar { it.uppercase() },
            valorBase = statDto.basestat
        )
    } ?: emptyList()
}


// 3. Movimentos
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


// 4. Evoluções (Corrigido para usar 'evo' e 'flatMap')
fun PokemonDto.toEvolutionEntities(): List<PokemonEvolutionEntity> {
    return this.evo?.flatMap { chain ->
        chain.evolution.map { namedResource ->
            PokemonEvolutionEntity(
                pokemonId = this.id,
                nomeEvolucao = namedResource.name.replaceFirstChar { it.uppercase() }
            )
        }
    } ?: emptyList()
}
