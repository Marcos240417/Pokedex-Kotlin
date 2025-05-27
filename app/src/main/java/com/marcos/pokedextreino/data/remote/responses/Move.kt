package com.marcos.pokedextreino.data.remote.responses

data class Move(
    val move: MoveX = MoveX(),
    val version_group_details: List<VersionGroupDetail> = listOf()
)