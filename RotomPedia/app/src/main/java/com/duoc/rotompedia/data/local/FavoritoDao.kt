package com.duoc.rotompedia.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {
    @Insert
    suspend fun agregarFavorito(favorito: PokemonFavorito)

    @Query("DELETE FROM favoritos WHERE pokemonId = :id")
    suspend fun eliminarFavorito(id: Int)

    // ✅ CORREGIDO: Método para eliminar por objeto (necesario para @Delete)
    @Delete
    suspend fun eliminarFavorito(favorito: PokemonFavorito)

    // Lo más importante: Obtener una lista reactiva de IDs de favoritos
    @Query("SELECT pokemonId FROM favoritos")
    fun obtenerTodosLosIds(): Flow<List<Int>>

    // ✅ AÑADIDO: Método para verificar si un Pokémon específico es favorito
    @Query("SELECT * FROM favoritos WHERE pokemonId = :pokemonId")
    suspend fun obtenerPorId(pokemonId: Int): PokemonFavorito?

    // ✅ AÑADIDO: Obtener todos los favoritos como objetos completos
    @Query("SELECT * FROM favoritos")
    fun obtenerTodosLosFavoritos(): Flow<List<PokemonFavorito>>
}