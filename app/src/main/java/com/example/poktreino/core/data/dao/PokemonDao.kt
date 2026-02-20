package com.example.poktreino.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.poktreino.core.data.datainfopokemon.PokemonEvolutionEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonMoveEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonStatEntity
import com.example.poktreino.core.data.datalocal.PokemonEntity
import com.example.poktreino.core.data.datalocal.PokemonWithDetails // Import do Relacionamento
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    // --- Inserções (Escrita) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: List<PokemonStatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoves(moves: List<PokemonMoveEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvolutions(evolutions: List<PokemonEvolutionEntity>)

    /**
     * @Transaction garante a atomicidade da escrita.
     * Ou salva o pacote completo do Pokémon, ou nada é salvo em caso de erro.
     */
    @Transaction
    suspend fun insertFullPokemon(
        pokemon: PokemonEntity,
        stats: List<PokemonStatEntity>,
        moves: List<PokemonMoveEntity>,
        evolutions: List<PokemonEvolutionEntity>
    ) {
        insertPokemon(pokemon)
        insertStats(stats)
        insertMoves(moves)
        insertEvolutions(evolutions)
    }

    // --- Consultas (Leitura) ---

    // Retorna a lista simplificada (usada na Home/Listagem)
    @Query("SELECT * FROM pokemons ORDER BY pokemonId ASC")
    fun getAllPokemons(): Flow<List<PokemonEntity>>

    /**
     * Retorna o Pokémon COMPLETO usando @Relation.
     * @Transaction aqui é necessário porque o Room executa múltiplas consultas
     * ocultas para reunir os dados das 4 tabelas.
     */
    @Transaction
    @Query("SELECT * FROM pokemons WHERE pokemonId = :id")
    fun getPokemonWithDetails(id: Int): Flow<PokemonWithDetails?>

    @Query("SELECT * FROM pokemons WHERE pokemonId = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    // --- Manutenção ---

    @Query("DELETE FROM pokemons")
    suspend fun clearAll()
}