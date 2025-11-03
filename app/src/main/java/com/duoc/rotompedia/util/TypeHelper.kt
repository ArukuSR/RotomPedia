package com.duoc.rotompedia.util

import androidx.compose.ui.graphics.Color

fun getTipoColor(tipo: String): Color {
    //Usamos el nombre en INGLES para la logica de colores
    //ya que el nombre traducido se puede cambiar
    return when (tipo.lowercase()) {
    "grass" -> Color(0xFF78C850)
    "fire" -> Color(0xFFF08030)
    "water" -> Color(0xFF6890F0)
    "bug" -> Color(0xFFA8B820)
    "normal" -> Color(0xFFA8A878)
    "poison" -> Color(0xFFA040A0)
    "electric" -> Color(0xFFF8D030)
    "ground" -> Color(0xFFE0C068)
    "fairy" -> Color(0xFFEE99AC)
    "fighting" -> Color(0xFFC03028)
    "psychic" -> Color(0xFFF85888)
    "rock" -> Color(0xFFB8A038)
    "ghost" -> Color(0xFF705898)
    "ice" -> Color(0xFF98D8D8)
    "dragon" -> Color(0xFF7038F8)
    "dark" -> Color(0xFF705848)
    "steel" -> Color(0xFFB8B8D0)
    "flying" -> Color(0xFFA890F0)
    else -> Color.Gray
    }
}