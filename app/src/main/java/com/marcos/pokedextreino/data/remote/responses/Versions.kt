package com.marcos.pokedextreino.data.remote.responses

data class Versions(
    val generation_i: GenerationI = GenerationI(),
    val generation_ii: GenerationIi = GenerationIi(),
    val generation_iii: GenerationIii = GenerationIii(),
    val generation_iv: GenerationIv = GenerationIv(),
    val generation_v: GenerationV = GenerationV(),
    val generation_vi: GenerationVi = GenerationVi(),
    val generation_vii: GenerationVii = GenerationVii(),
    val generation_viii: GenerationViii = GenerationViii()
)