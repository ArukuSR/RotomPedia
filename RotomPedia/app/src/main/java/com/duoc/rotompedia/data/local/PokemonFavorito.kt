package com.duoc.rotompedia.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class PokemonFavorito(
    // Usamos el ID del Pokémon como PrimaryKey para evitar duplicados
    @PrimaryKey val pokemonId: Int,
    val nombre: String
)