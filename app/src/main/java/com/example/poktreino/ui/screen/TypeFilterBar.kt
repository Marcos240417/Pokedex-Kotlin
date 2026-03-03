package com.example.poktreino.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TypeFilterBar(
    selectedType: String?,
    onTypeSelected: (String?) -> Unit
) {
    val types = listOf(
        "Normal",
        "Fire",
        "Water",
        "Grass",
        "Electric",
        "Ice",
        "Fighting",
        "Poison",
        "Ground",
        "Flying",
        "Psychic",
        "Bug",
        "Rock",
        "Ghost",
        "Dragon",
        "Dark",
        "Steel",
        "Fairy"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedType == null,
                onClick = { onTypeSelected(null) },
                label = { Text("Todos") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFE91E63),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White.copy(alpha = 0.1f),
                    labelColor = Color.White.copy(alpha = 0.7f)
                ),
                border = null,
                shape = RoundedCornerShape(20.dp)
            )
        }
        items(types) { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(if (selectedType == type) null else type) },
                label = { Text(type) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFE91E63), // Cor rosa que você já usa
                    selectedLabelColor = Color.White,
                    containerColor = Color.White.copy(alpha = 0.1f),
                    labelColor = Color.White.copy(alpha = 0.7f)
                ),
                border = null,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}