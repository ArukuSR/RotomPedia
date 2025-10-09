package com.duoc.rotompedia

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Se usara JsonClass para que moshi cree el codigo que convierte la respuesta JSON de la api en este objeto kotlin

@JsonClass(generateAdapter = true)
data class  PokemonData(
    val id: Int,
    val name:String,
    val sprites: SpritesData,
    val types: List<TypesSlot>
)

//Clases anidadas para las Imagenes
@JsonClass(generateAdapter = true)
data class SpritesData(
    //nombre de la propiedad en la Api es 'front_default'
    //se mapea en 'urlImagen' para que sea claro
    @Json(name = "front_default") val urlImagen: String?
)

//Clases anidadas para los tipos
@JsonClass(generateAdapter =true)
data class TypesSlot(
    val slot: Int,
    val type: TypeData
)

@JsonClass(generateAdapter = true)
data class TypeData(
    val name: String
)

//Soporte para la Interfaz UI
//Se usara más adelante para simplificar los datos mostrados
data class PokemonDisplayData(
    val id: Int,
    val nombre: String,
    val urlImagen: String,
    val tipos: List<String>
)