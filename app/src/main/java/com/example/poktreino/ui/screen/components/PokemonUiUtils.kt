package com.example.poktreino.ui.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn (ExperimentalMaterial3Api::class)
@Composable
fun getGradientForType(type: String): Brush {
    val colors = when (type.lowercase()) {
        "grass" -> listOf(Color(0xFF78C850), Color(0xFF4A148C)) // Verde + Roxo
        "fire" -> listOf(Color(0xFFF08030), Color(0xFF421100))  // Laranja + Vermelho Escuro
        "water" -> listOf(Color(0xFF6890F0), Color(0xFF001242)) // Azul + Azul Marinho
        "bug" -> listOf(Color(0xFFA8B820), Color(0xFF1B5E20))   // Inseto
        "poison" -> listOf(Color(0xFFA040A0), Color(0xFF4A148C))// Veneno
        "electric" -> listOf(Color(0xFFF8D030), Color(0xFF5E4900))
        "ground" -> listOf(Color(0xFFE0C068), Color(0xFF4E3B00))
        "fairy" -> listOf(Color(0xFFEE99AC), Color(0xFF770022))
        "fighting" -> listOf(Color(0xFFC03028), Color(0xFF480000))
        "psychic" -> listOf(Color(0xFFF85888), Color(0xFF4D0021))
        "rock" -> listOf(Color(0xFFB8A038), Color(0xFF2B2500))
        "ghost" -> listOf(Color(0xFF705898), Color(0xFF221144))
        "ice" -> listOf(Color(0xFF98D8D8), Color(0xFF004A4A))
        "dragon" -> listOf(Color(0xFF7038F8), Color(0xFF220055))
        "dark" -> listOf(Color(0xFF705848), Color(0xFF1F1200))
        "steel" -> listOf(Color(0xFFB8B8D0), Color(0xFF2B2B3D))
        "flying" -> listOf(Color(0xFFA890F0), Color(0xFF221144))
        "normal" -> listOf(Color(0xFFA8A878), Color(0xFF2D2D1B))
        else -> listOf(Color(0xFFB8B8D0), Color(0xFF212121))
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
}

@Composable
fun InfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
@OptIn (ExperimentalMaterial3Api::class)
@Composable
fun TypeBadge(type: String) {
    val (badgeColor, icon) = when (type.lowercase()) {
        "grass" -> Color(0xFF2ECC71) to Icons.Default.Eco
        "fire" -> Color(0xFFE74C3C) to Icons.Default.LocalFireDepartment
        "water" -> Color(0xFF3498DB) to Icons.Default.WaterDrop
        "poison" -> Color(0xFF9B59B6) to Icons.Default.Science
        else -> Color(0xFF95A5A6) to Icons.Default.CatchingPokemon
    }
    Surface(modifier = Modifier.padding(end = 6.dp), shape = CircleShape, color = badgeColor.copy(alpha = 0.85f)) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = type.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
    }
}