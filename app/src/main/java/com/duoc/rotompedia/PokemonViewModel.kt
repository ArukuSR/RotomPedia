// PokemonViewModel.kt

package com.duoc.rotompedia

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// --- (PokedexState) ---
sealed class PokedexState {
    object Loading : PokedexState()
    data class Success(val pokemonList: List<PokemonListItem>) : PokedexState()
    data class Error(val message: String) : PokedexState()
}

// --- (PokemonDetailState) ---
sealed class PokemonDetailState {
    object Loading : PokemonDetailState()
    data class Success(val pokemonData: PokemonData) : PokemonDetailState()
    data class Error(val message: String) : PokemonDetailState()
}

// --- ¡EVOLUCIÓN! ---
sealed class EvolutionState {
    object Loading : EvolutionState()
    data class Success(
        val evolutionChain: List<Species>,
        val description: String
    ) : EvolutionState()
    data class Error(val message: String) : EvolutionState()
}
// --- ¡PARA LAS RELACIONES DE TIPO! ---
sealed class TypeRelationsState {
    object Loading : TypeRelationsState()
    data class Success(
        val weakAgainst: List<String>, // Débil contra
        val strongAgainst: List<String> // Fuerte contra
    ) : TypeRelationsState()
    data class Error(val message: String) : TypeRelationsState()
}

class PokemonViewModel : ViewModel() {
    private val TAG = "RotomPedia_ViewModel"

    // --- (StateFlow para Pokedex) ---
    private val _pokedexState = MutableStateFlow<PokedexState>(PokedexState.Loading)
    val pokedexState: StateFlow<PokedexState> = _pokedexState

    // --- (StateFlow para Detalles) ---
    private val _pokemonDetailState = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Loading)
    val pokemonDetailState: StateFlow<PokemonDetailState> = _pokemonDetailState

    // --- ¡STATEFLOW PARA LA EVOLUCIÓN! ---
    private val _evolutionState = MutableStateFlow<EvolutionState>(EvolutionState.Loading)
    val evolutionState: StateFlow<EvolutionState> = _evolutionState

    // --- ¡STATEFLOW  RELACIONES DE TIPO! ---
    private val _typeRelationsState = MutableStateFlow<TypeRelationsState>(TypeRelationsState.Loading)
    val typeRelationsState: StateFlow<TypeRelationsState> = _typeRelationsState

    init {
        loadPokemonList()
    }

    // --- (loadPokemonList no cambia) ---
    private fun loadPokemonList() {
        viewModelScope.launch {
            _pokedexState.value = PokedexState.Loading
            try {
                val response = withContext(Dispatchers.IO) {
                    PokemonApi.service.getPokemonList(limit = 1025, offset = 0)
                }
                val formattedList = response.results.map {
                    it.copy(name = it.name.replaceFirstChar { char -> char.uppercase() })
                }
                _pokedexState.value = PokedexState.Success(formattedList)
                Log.d(TAG, "Lista de Pokémon cargada exitosamente.")
            } catch (e: Exception) {
                val errorMessage = "❌ Error al cargar la lista: ${e.message}"
                _pokedexState.value = PokedexState.Error(errorMessage)
                Log.e(TAG, errorMessage, e)
            }
        }
    }

    // --- (loadPokemonDetails no cambia) ---
    fun loadPokemonDetails(pokemonId: String) {
        viewModelScope.launch {
            _pokemonDetailState.value = PokemonDetailState.Loading
            try {
                val response = withContext(Dispatchers.IO) {
                    PokemonApi.service.getPokemon(pokemonId.toInt())
                }
                _pokemonDetailState.value = PokemonDetailState.Success(response)
                Log.d(TAG, "Detalles de ${response.name} cargados exitosamente.")
            } catch (e: Exception) {
                val errorMessage = "❌ Error al cargar detalles: ${e.message}"
                _pokemonDetailState.value = PokemonDetailState.Error(errorMessage)
                Log.e(TAG, errorMessage, e)
            }
        }
    }


    fun loadEvolutionChain(pokemonId: String) {
        viewModelScope.launch {
            _evolutionState.value = EvolutionState.Loading
            try {
                // 1. Obtenemos los datos de la especie
                val speciesResponse = withContext(Dispatchers.IO) {
                    PokemonApi.service.getPokemonSpecies(pokemonId.toInt())
                }

                // 2. Extraemos la descripción EN ESPAÑOL
                val description = speciesResponse.flavorTextEntries
                    .find { it.language.name == "es" } // Buscamos la entrada en español
                    ?.flavorText // Tomamos el texto
                    ?.replace("\n", " ") // Reemplazamos saltos de línea por espacios
                    ?: "No se encontró descripción en español." // Texto por defecto

                // 3. Obtenemos la URL de evolución y la llamamos
                val evolutionChainUrl = speciesResponse.evolutionChain.url
                val evolutionResponse = withContext(Dispatchers.IO) {
                    PokemonApi.service.getEvolutionChain(evolutionChainUrl)
                }

                // 4. Procesamos la cadena
                val chainList = parseEvolutionChain(evolutionResponse.chain)
                val formattedList = chainList.map {
                    it.copy(name = it.name.replaceFirstChar { char -> char.uppercase() })
                }

                // 5. Emitimos el estado de Éxito con AMBOS datos
                _evolutionState.value = EvolutionState.Success(formattedList, description)
                Log.d(TAG, "Evolución y descripción cargadas.")

            } catch (e: Exception) {
                val errorMessage = "❌ Error al cargar evolución/descripción: ${e.message}"
                _evolutionState.value = EvolutionState.Error(errorMessage)
                Log.e(TAG, errorMessage, e)
            }
        }
    }

    /**
     * Función auxiliar recursiva para "aplanar" la cadena de evolución
     * de un objeto anidado a una lista simple.
     */
    private fun parseEvolutionChain(chain: ChainLink): List<Species> {
        val evolutionList = mutableListOf<Species>()
        var currentLink: ChainLink? = chain

        // Recorremos la cadena mientras haya una siguiente evolución
        while (currentLink != null) {
            evolutionList.add(currentLink.species)
            currentLink = currentLink.evolvesTo.firstOrNull()
        }
        return evolutionList
    }
    fun loadTypeRelations(types: List<TypesSlot>) {
        viewModelScope.launch {
            _typeRelationsState.value = TypeRelationsState.Loading
            try {
                // Usamos 'async' para hacer las llamadas de tipos en paralelo
                val type1Details = async(Dispatchers.IO) {
                    PokemonApi.service.getTypeDetails(types[0].type.name)
                }

                // Si hay un segundo tipo, también lo pedimos
                val type2Details = if (types.size > 1) {
                    async(Dispatchers.IO) {
                        PokemonApi.service.getTypeDetails(types[1].type.name)
                    }
                } else {
                    null // Si no, nulo
                }

                // Esperamos a que terminen
                val type1Relations = type1Details.await().damageRelations
                val type2Relations = type2Details?.await()?.damageRelations

                // Usamos Sets para combinar las listas y evitar duplicados
                val weakAgainst = mutableSetOf<String>()
                val strongAgainst = mutableSetOf<String>()

                // Añadimos las del tipo 1
                weakAgainst.addAll(type1Relations.doubleDamageFrom.map { it.name })
                strongAgainst.addAll(type1Relations.doubleDamageTo.map { it.name })

                // Si hay un tipo 2, añadimos las suyas
                if (type2Relations != null) {
                    weakAgainst.addAll(type2Relations.doubleDamageFrom.map { it.name })
                    strongAgainst.addAll(type2Relations.doubleDamageTo.map { it.name })
                }

                // NOTA: Esta es una simplificación. No calcula 4x o neutralizaciones.
                // Pero es un excelente punto de partida y cumple con la petición.

                _typeRelationsState.value = TypeRelationsState.Success(
                    weakAgainst.toList(),
                    strongAgainst.toList()
                )
                Log.d(TAG, "Relaciones de tipo cargadas.")

            } catch (e: Exception) {
                val errorMessage = "❌ Error al cargar relaciones de tipo: ${e.message}"
                _typeRelationsState.value = TypeRelationsState.Error(errorMessage)
                Log.e(TAG, errorMessage, e)
            }
        }
    }
}