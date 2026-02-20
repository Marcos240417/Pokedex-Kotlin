package com.example.poktreino.core.data.datalocal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.poktreino.core.data.dao.PokemonDao
import com.example.poktreino.core.data.datainfopokemon.PokemonEvolutionEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonMoveEntity
import com.example.poktreino.core.data.datainfopokemon.PokemonStatEntity

// 1. Registe todas as 4 entidades aqui
@Database(
    entities = [
        PokemonEntity::class,
        PokemonStatEntity::class,
        PokemonMoveEntity::class,
        PokemonEvolutionEntity::class
    ],
    version = 1, // Aumente este número se mudar a estrutura das tabelas
    exportSchema = false
)
abstract class PoketreinoDatabase : RoomDatabase() {

    // 2. Define o acesso ao seu DAO
    abstract val pokemonDao: PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PoketreinoDatabase? = null

        /**
         * Padrão Singleton: Garante que apenas uma instância do banco exista.
         */
        fun getDatabase(context: Context): PoketreinoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PoketreinoDatabase::class.java,
                    "poketreino_db" // Nome do ficheiro SQLite no telemóvel
                )
                    .fallbackToDestructiveMigration() // Útil durante o desenvolvimento
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}