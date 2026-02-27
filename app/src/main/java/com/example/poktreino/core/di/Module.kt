package com.example.poktreino.core.di

import com.example.poktreino.ui.viewmodel.PokemonDetailViewModel
import com.example.poktreino.core.data.datalocal.PoketreinoDatabase
import com.example.poktreino.core.data.remote.PokeApiService
import com.example.poktreino.core.data.domain.repository.PokemonRepositoryImpl
import com.example.poktreino.core.data.domain.repository.PokemonRepository

import com.example.poktreino.ui.viewmodel.PokemonViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // 1. Singleton do Retrofit e API
    single {
        Retrofit.Builder()
            .baseUrl(PokeApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }

    // 2. Singleton do Database e DAO
    single { PoketreinoDatabase.getDatabase(get()) }
    single { get<PoketreinoDatabase>().pokemonDao }

    // 3. Repositório (Vinculamos a Interface à Implementação)
    single<PokemonRepository> { PokemonRepositoryImpl(api = get(), dao = get()) }

    // 4. ViewModel (O Koin cuida do ciclo de vida)
    viewModel { PokemonViewModel(repository = get()) }
    viewModel { PokemonDetailViewModel(repository = get()) }
}