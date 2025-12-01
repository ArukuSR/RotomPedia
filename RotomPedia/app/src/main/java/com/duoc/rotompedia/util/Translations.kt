package com.duoc.rotompedia.util

/**
 * Traduce un nombre de tipo de Pokémon de inglés a español.
 */
fun traducirTipo(tipoEnIngles: String): String {
    return when (tipoEnIngles.lowercase()) {
        "normal" -> "Normal"
        "fire" -> "Fuego"
        "water" -> "Agua"
        "electric" -> "Eléctrico"
        "grass" -> "Planta"
        "ice" -> "Hielo"
        "fighting" -> "Lucha"
        "poison" -> "Veneno"
        "ground" -> "Tierra"
        "flying" -> "Volador"
        "psychic" -> "Psíquico"
        "bug" -> "Bicho"
        "rock" -> "Roca"
        "ghost" -> "Fantasma"
        "dragon" -> "Dragón"
        "dark" -> "Siniestro"
        "steel" -> "Acero"
        "fairy" -> "Hada"
        else -> tipoEnIngles.replaceFirstChar { it.uppercase() } // Si no lo encuentra, solo lo capitaliza
    }
}