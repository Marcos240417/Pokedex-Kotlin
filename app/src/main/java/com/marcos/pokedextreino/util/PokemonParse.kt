package com.marcos.pokedextreino.util

import androidx.compose.ui.graphics.Color
import com.marcos.pokedextreino.data.remote.responses.Stat
import com.marcos.pokedextreino.data.remote.responses.Type
import com.marcos.pokedextreino.ui.theme.AtkColor
import com.marcos.pokedextreino.ui.theme.DefColor
import com.marcos.pokedextreino.ui.theme.HPColor
import com.marcos.pokedextreino.ui.theme.SpAtkColor
import com.marcos.pokedextreino.ui.theme.SpDefColor
import com.marcos.pokedextreino.ui.theme.SpdColor
import com.marcos.pokedextreino.ui.theme.TypeBug
import com.marcos.pokedextreino.ui.theme.TypeDark
import com.marcos.pokedextreino.ui.theme.TypeDragon
import com.marcos.pokedextreino.ui.theme.TypeElectric
import com.marcos.pokedextreino.ui.theme.TypeFairy
import com.marcos.pokedextreino.ui.theme.TypeFighting
import com.marcos.pokedextreino.ui.theme.TypeFire
import com.marcos.pokedextreino.ui.theme.TypeFlying
import com.marcos.pokedextreino.ui.theme.TypeGhost
import com.marcos.pokedextreino.ui.theme.TypeGrass
import com.marcos.pokedextreino.ui.theme.TypeGround
import com.marcos.pokedextreino.ui.theme.TypeIce
import com.marcos.pokedextreino.ui.theme.TypeNormal
import com.marcos.pokedextreino.ui.theme.TypePoison
import com.marcos.pokedextreino.ui.theme.TypePsychic
import com.marcos.pokedextreino.ui.theme.TypeRock
import com.marcos.pokedextreino.ui.theme.TypeSteel
import com.marcos.pokedextreino.ui.theme.TypeWater
import java.util.Locale


fun parseTypeToColor(type: Type): Color {
    return when(type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.lowercase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}