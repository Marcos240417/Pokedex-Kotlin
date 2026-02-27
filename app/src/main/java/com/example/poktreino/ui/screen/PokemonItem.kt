package com.example.poktreino.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.ui.screen.components.TypeBadge
import com.example.poktreino.ui.screen.components.getGradientForType

@Composable
fun PokemonItem(pokemon: PokemonEntity, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onItemClick(pokemon.pokemonId) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getGradientForType(pokemon.tipoPrincipal))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(130.dp)
                ) {
                    AsyncImage(
                        model = pokemon.urlImagem,
                        contentDescription = pokemon.nome,

                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "#${pokemon.pokemonId.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pokemon.nome,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp
                    )

                    Row(modifier = Modifier.padding(vertical = 10.dp)) {
                        TypeBadge(type = pokemon.tipoPrincipal)
                        pokemon.tipoSecundario?.let {
                            Spacer(Modifier.width(8.dp))
                            TypeBadge(type = it)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoItem(Icons.Default.MonitorWeight, "${pokemon.peso / 10.0} kg", "Peso")
                        InfoItem(Icons.Default.Height, "${pokemon.altura / 10.0} m", "Altura")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.White.copy(alpha = 0.9f)
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}