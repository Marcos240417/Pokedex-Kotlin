package com.example.poktreino.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.ui.screen.components.InfoColumn

import com.example.poktreino.ui.screen.components.TypeBadge
import com.example.poktreino.ui.screen.components.getGradientForType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonItem(pokemon: PokemonEntity, onItemClick: (Int) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(pokemon.pokemonId) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.background(getGradientForType(pokemon.tipoPrincipal))) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coluna da Esquerda (Imagem + ‘ID’)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = pokemon.urlImagem,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                    Text(
                        text = "#${pokemon.pokemonId.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Coluna da Direita (Nome + Badges + Info)
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = pokemon.nome,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )

                    Row(modifier = Modifier.padding(vertical = 12.dp)) {
                        TypeBadge(type = pokemon.tipoPrincipal)
                        pokemon.tipoSecundario?.let {
                            Spacer(modifier = Modifier.width(8.dp))
                            TypeBadge(type = it)
                        }
                    }

                    // Glassmorphism para Peso e Altura
                    Surface(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            InfoColumn(label = "Peso", value = "${pokemon.peso / 10.0} kg")
                            InfoColumn(label = "Altura", value = "${pokemon.altura / 10.0} m")
                        }
                    }
                }
            }
        }
    }
}