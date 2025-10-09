package com.duoc.rotompedia

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

//Definir los posibles estados de conexion para mostrar en la UI
sealed class ApiState{
    object Loading: ApiState()
    class Success(val info: String): ApiState()
    data class Error(val message: String): ApiState()
}

//viewmodel maneja la logica, api y expone el estado a la UI
class PokemonViewModel: ViewModel() {
    private val TAG = "RotomPedia_API"

    private val _apiState = MutableStateFlow<ApiState>(ApiState.Loading)
    val apiState: StateFlow<ApiState> = _apiState //Expone el estado de la UI

    init {
        //intentara obtener al pokemon al crear el viewmodel
        testApiConnection()
    }

    //llama la api de forma segura, usando coroutines
    private fun testApiConnection(){
        viewModelScope.launch {
            try {
                //1 Ejecuta una llamada a la api en el hilo de ejecucion I/O
                val pokemonData = withContext(Dispatchers.IO){
                    PokemonApi.service.getPokemon(1)
                }

                //2 procesar y preparar el resultado
                val nombreCapitalizado = pokemonData.name.replaceFirstChar { it.uppercase()}
                val tipos = pokemonData.types.joinToString(", "){typeSlot ->
                    typeSlot.type.name.replaceFirstChar { it.uppercase() }
                }

                val resultadoFinal = "✅ ¡Conexion Exitosa!\n" +
                                     "Pokemon ID ${pokemonData.id}: $nombreCapitalizado\n" +
                                     "Tipos: $tipos"

                //3 actualizar el estado Succes (La UI se actualizara automaticamente)
                _apiState.value = ApiState.Success(resultadoFinal)
                Log.d(TAG, "Resultado: $resultadoFinal")

            } catch (e: Exception){
                //4 manejar errores
                val errorMessage = "❌ Error de conexion: ${e.message}"
                _apiState.value = ApiState.Error(errorMessage)
                Log.e(TAG, errorMessage, e)
            }
        }
    }
}