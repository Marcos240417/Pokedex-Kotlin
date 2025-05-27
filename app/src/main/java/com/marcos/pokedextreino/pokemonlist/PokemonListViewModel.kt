package com.marcos.pokedextreino.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.marcos.pokedextreino.data.model.PokedexListEntry
import com.marcos.pokedextreino.repository.PokemonRepository
import com.marcos.pokedextreino.util.Constants.PAGE_SIZE
import com.marcos.pokedextreino.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    private val _pokemonList = MutableStateFlow<List<PokedexListEntry>>(emptyList())
    val pokemonList: StateFlow<List<PokedexListEntry>> get() = _pokemonList

    private val _loadError = mutableStateOf("")
    val loadError: State<String> = _loadError

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _endReached = mutableStateOf(false)
    val endReached: State<Boolean> = _endReached

    private var cachedPokemonList = listOf<PokedexListEntry>()

    private val _isSearchStarting = mutableStateOf(true)
    val isSearchStarting: State<Boolean> get() = _isSearchStarting

    private val _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean> get() = _isSearching


    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchStarting.value) {
            _pokemonList.value
        } else {
            cachedPokemonList
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (query.isEmpty()) {
                _pokemonList.value = cachedPokemonList
                _isSearching.value = false
                _isSearchStarting.value = true
                return@launch
            }

            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }

            if (isSearchStarting.value) {
                cachedPokemonList = _pokemonList.value
                _isSearchStarting.value = false
            }

            _pokemonList.value = results
            _isSearching.value = true
        }
    }




    fun loadPokemonPaginated() {
        // Evita múltiplas chamadas ou requisições após o fim da lista
        if (_isLoading.value || _endReached.value) return

        viewModelScope.launch {
            _isLoading.value = true

            val result = try {
                repository.getPokemonList(limit = PAGE_SIZE, offset = curPage * PAGE_SIZE)
            } catch (e: Exception) {
                _isLoading.value = false
                _loadError.value = "Erro de conexão: ${e.localizedMessage ?: "Erro desconhecido"}"
                return@launch
            }

            when (result) {
                is Resource.Success -> {
                    result.data?.let { data ->
                        _endReached.value = curPage * PAGE_SIZE >= data.count

                        val entries = data.results.map { entry ->
                            val number = entry.url?.trimEnd('/')?.takeLastWhile { it.isDigit() } ?: "0"
                            val imageUrl =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"
                            PokedexListEntry(
                                pokemonName = entry.name.replaceFirstChar { it.uppercase() },
                                imageUrl = imageUrl,
                                number = number.toIntOrNull() ?: 0
                            )
                        }

                        curPage++
                        _pokemonList.value += entries
                        _loadError.value = ""
                    }
                }

                is Resource.Error -> {
                    _loadError.value = result.message ?: "Erro desconhecido ao carregar dados."
                }

                is Resource.Loading -> {
                    // Esse estado não deveria ocorrer aqui, pois estamos *definindo* como carregar os dados.
                    // No entanto, adicionamos para satisfazer o compilador e manter o código robusto.
                }
            }

            _isLoading.value = false
        }
    }



    fun calcDominantColor(drawable: Drawable, onFinish: (Int) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(colorValue)
            }
        }
    }
}


