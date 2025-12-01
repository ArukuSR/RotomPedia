// PokemonViewModelFactory.kt
package com.duoc.rotompedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duoc.rotompedia.data.local.FavoritoDao

// Factory para inyectar FavoritoDao en PokemonViewModel
class PokemonViewModelFactory(
    private val favoritoDao: FavoritoDao
) : ViewModelProvider.Factory {

    // Sobreescribe el método para crear la instancia del ViewModel
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            return PokemonViewModel(favoritoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}