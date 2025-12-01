package com.duoc.rotompedia.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    // Inserta un nuevo usuario. Si ya existe, lo reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    // Busca un usuario por su nombre de usuario
    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombreUsuario LIMIT 1")
    suspend fun obtenerUsuarioPorNombre(nombreUsuario: String): Usuario?

    // Busca un usuario por su correo electrónico
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?
}
