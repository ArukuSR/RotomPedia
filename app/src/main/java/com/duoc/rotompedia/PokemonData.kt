package com.duoc.rotompedia

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Se usara JsonClass para que moshi cree el codigo que convierte la respuesta JSON de la api en este objeto kotlin

@JsonClass(generateAdapter = true)
data class  PokemonData(
    val id: Int,
    val name:String,
    val sprites: SpritesData,
    val types: List<TypesSlot>,
    val stats: List<StatInfo>
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
// Representa la respuesta completa de la API cuando pedimos una lista
@JsonClass(generateAdapter = true)
data class PokemonListResponse(
    val results: List<PokemonListItem>
)

//Representa cada Pokémon individual dentro de la lista
@JsonClass(generateAdapter = true)
data class PokemonListItem(
    val name: String,
    val url: String
)
//Contiene la información de una estadística individual
@JsonClass(generateAdapter = true)
data class StatInfo(
    // El valor de la estadística (ej: 45)
    @Json(name = "base_stat") val baseStat: Int,
    // El objeto que contiene el nombre de la estadística
    val stat: Stat
)

//Contiene el nombre de la estadística (ej: "hp")
@JsonClass(generateAdapter = true)
data class Stat(
    val name: String
)

// 1. Para obtener la URL de la cadena de evolución
@JsonClass(generateAdapter = true)
data class PokemonSpeciesResponse(
    @Json(name = "evolution_chain") val evolutionChain: EvolutionChainUrl,
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>

)

@JsonClass(generateAdapter = true)
data class EvolutionChainUrl(
    val url: String
)

// 2. Para procesar la cadena de evolución (que es recursiva)
@JsonClass(generateAdapter = true)
data class EvolutionChainResponse(
    val chain: ChainLink
)

@JsonClass(generateAdapter = true)
data class ChainLink(
    val species: Species,
    @Json(name = "evolves_to") val evolvesTo: List<ChainLink>
)

// 3. 'Species' es similar a 'PokemonListItem', pero lo usamos
// para ser claros con lo que recibimos de la API
@JsonClass(generateAdapter = true)
data class Species(
    val name: String,
    val url: String
)

// Para la descripción (flavor text)
@JsonClass(generateAdapter = true)
data class FlavorTextEntry(
    @Json(name = "flavor_text") val flavorText: String,
    val language: Language
)

@JsonClass(generateAdapter = true)
data class Language(
    val name: String
)

// --- CLASES NUEVAS PARA LAS RELACIONES DE TIPO ---

// La respuesta completa de la API para un tipo
@JsonClass(generateAdapter = true)
data class TypeDetailResponse(
    @Json(name = "damage_relations") val damageRelations: DamageRelations
)

// Contiene las listas de debilidades, fortalezas, etc.
@JsonClass(generateAdapter = true)
data class DamageRelations(
    @Json(name = "double_damage_from") val doubleDamageFrom: List<TypeRelation>,
    @Json(name = "double_damage_to") val doubleDamageTo: List<TypeRelation>
    // Podríamos añadir half_damage_from, no_damage_from, etc. en el futuro
)

// Representa un tipo individual en las listas de relaciones
@JsonClass(generateAdapter = true)
data class TypeRelation(
    val name: String
)