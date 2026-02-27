package com.example.poktreino.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.poktreino.ui.screen.PokemonDetailScreen
import com.example.poktreino.ui.screen.PokemonListScreen

@Composable
fun PokedexNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "pokemon_list") {
        composable("pokemon_list") {
            PokemonListScreen(onPokemonClick = { id ->
                navController.navigate("pokemon_detail/$id")
            })
        }

        composable(
            route = "pokemon_detail/{pokemonId}",
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("pokemonId") ?: 0
            PokemonDetailScreen(
                pokemonId = id,
                onBackClick = { navController.popBackStack() },
                onEvolutionClick = { evoId ->
                    navController.navigate("pokemon_detail/$evoId") {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}