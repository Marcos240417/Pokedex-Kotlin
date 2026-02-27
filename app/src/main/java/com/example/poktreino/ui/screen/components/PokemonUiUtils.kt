package com.example.poktreino.ui.screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun getGradientForType(type: String): Brush {
    val typeColor = when (type.lowercase()) {
        "grass" -> Color(0xFF78C850)
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "bug" -> Color(0xFFA8B820)
        "poison" -> Color(0xFFA040A0)
        "electric" -> Color(0xFFF8D030)
        "ground" -> Color(0xFFE0C068)
        "fairy" -> Color(0xFFEE99AC)
        "fighting" -> Color(0xFFC03028)
        "psychic" -> Color(0xFFF85888)
        "rock" -> Color(0xFFB8A038)
        "ghost" -> Color(0xFF705898)
        "ice" -> Color(0xFF98D8D8)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB8B8D0)
        "flying" -> Color(0xFFA890F0)
        "normal" -> Color(0xFFA8A878)
        else -> Color(0xFF95A5A6)
    }

    return Brush.horizontalGradient(
        colors = listOf(typeColor.copy(alpha = 0.5f), Color(0xFF121212)),
        startX = 0f,
        endX = 600f
    )
}

@Composable
fun TypeBadge(type: String) {
    val (badgeColor, icon) = when (type.lowercase()) {
        "grass" -> Color(0xFF78C850) to Icons.Default.Eco
        "fire" -> Color(0xFFF08030) to Icons.Default.LocalFireDepartment
        "water" -> Color(0xFF6890F0) to Icons.Default.WaterDrop
        "poison" -> Color(0xFFA040A0) to Icons.Default.Science
        else -> Color(0xFF95A5A6) to Icons.Default.CatchingPokemon
    }

    Surface(
        modifier = Modifier.padding(end = 6.dp),
        shape = RoundedCornerShape(8.dp),
        color = badgeColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, Modifier.size(14.dp), Color.White)
            Spacer(Modifier.width(4.dp))
            Text(type.uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
    }
}