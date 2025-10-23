// PokemonDetailScreen.kt

package com.duoc.rotompedia

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
private fun getStatColor(value: Int): Color {
    return when {
        value < 50 -> Color(0xFFF44336) // Rojo
        value < 80 -> Color(0xFFFF9800) // Naranja
        value < 110 -> Color(0xFF4CAF50) // Verde
        else -> Color(0xFF2196F3) // Azul (para stats muy altos)
    }
}

@Composable
fun PokemonDetailScreen(pokemonId: String, pokemonViewModel: PokemonViewModel = viewModel()) {
    // Carga los detalles del Pokémon
    LaunchedEffect(key1 = pokemonId) {
        pokemonViewModel.loadPokemonDetails(pokemonId)
        pokemonViewModel.loadEvolutionChain(pokemonId)
    }

    val detailState by pokemonViewModel.pokemonDetailState.collectAsState()
    val evolutionState by pokemonViewModel.evolutionState.collectAsState()
    val typeRelationsState by pokemonViewModel.typeRelationsState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = detailState) {
            is PokemonDetailState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is PokemonDetailState.Success -> {
                LaunchedEffect(key1 = currentState.pokemonData.types) {
                    pokemonViewModel.loadTypeRelations(currentState.pokemonData.types)
                }

                PokemonDetailContent(
                    pokemon = currentState.pokemonData,
                    evolutionState = evolutionState,
                    typeRelationsState = typeRelationsState
                )
            }
            is PokemonDetailState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun PokemonDetailContent(
    pokemon: PokemonData,
    evolutionState: EvolutionState,
    typeRelationsState: TypeRelationsState// <-- NUEVO
) {
    // Hacemos la columna "scrollable" por si el contenido no cabe
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // <-- NUEVO
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png")
                .crossfade(true)
                .build(),
            contentDescription = "Imagen de ${pokemon.name}",
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "#${pokemon.id} - ${pokemon.name.replaceFirstChar { it.uppercase() }}",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemon.types.forEach { typeSlot ->
                TypeChip(type = typeSlot.type.name)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- ¡NUEVA SECCIÓN DE DESCRIPCIÓN! ---
        Text(
            text = "Descripción",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Mostramos la descripción cuando el estado de evolución sea Exitoso
        if (evolutionState is EvolutionState.Success) {
            Text(
                text = evolutionState.description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Un color de texto más suave
                textAlign = TextAlign.Justify
            )
        } else {
            // Mostramos un indicador de carga si aún no ha llegado
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- Sección de Estadísticas ---
        Text(
            text = "Estadísticas Base",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemon.stats.forEach { statInfo ->
                StatRow(statInfo = statInfo)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- ¡NUEVA SECCIÓN DE RELACIONES DE TIPO! ---
        Text(
            text = "Relaciones de Tipo",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        TypeRelationsView(state = typeRelationsState)
        Spacer(modifier = Modifier.height(24.dp))

        // --- ¡NUEVA SECCIÓN DE EVOLUCIÓN! ---
        Text(
            text = "Línea Evolutiva",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Mostramos el estado de la evolución
        when (evolutionState) {
            is EvolutionState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is EvolutionState.Success -> {
                EvolutionChainRow(chain = evolutionState.evolutionChain)
            }
            is EvolutionState.Error -> {
                Text(
                    text = evolutionState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Espacio al final
    }
}

// --- (TypeChip no cambia) ---
@Composable
fun TypeChip(type: String) {
    val typeColor = when (type) {
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
    Box(
        modifier = Modifier
            .background(typeColor, CircleShape)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = type.uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

// --- ¡STATROW MODIFICADO PARA USAR EL COLOR DINÁMICO! ---
@Composable
fun StatRow(statInfo: StatInfo) {
    val statName = when (statInfo.stat.name) {
        "hp" -> "HP"
        "attack" -> "Ataque"
        "defense" -> "Defensa"
        "special-attack" -> "At. Esp."
        "special-defense" -> "Def. Esp."
        "speed" -> "Velocidad"
        else -> statInfo.stat.name.replaceFirstChar { it.uppercase() }
    }

    // Obtenemos el color basado en el valor
    val statColor = getStatColor(statInfo.baseStat)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            modifier = Modifier.width(80.dp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = statInfo.baseStat.toString(),
            modifier = Modifier.width(40.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.width(8.dp))
        LinearProgressIndicator(
            progress = { statInfo.baseStat / 255f },
            modifier = Modifier
                .height(12.dp)
                .clip(CircleShape),
            color = statColor, // <-- ¡CAMBIO PRINCIPAL! Usamos el color dinámico
            trackColor = MaterialTheme.colorScheme.surfaceVariant // Color de fondo de la barra
        )
    }
}



// --- ¡NUEVO COMPOSABLE PARA LA FILA DE EVOLUCIÓN! ---
@Composable
fun EvolutionChainRow(chain: List<Species>) {
    // Si no hay evoluciones (ej: Tauros), muestra un texto
    if (chain.size <= 1) {
        Text(
            text = "Este Pokémon no evoluciona.",
            color = MaterialTheme.colorScheme.onBackground
        )
        return
    }

    // Hacemos que la fila sea "scrollable" horizontalmente
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), // <-- NUEVO
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        chain.forEachIndexed { index, species ->
            // Extrae el ID de la URL
            val pokemonId = species.url.split("/").dropLast(1).last()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                // Imagen
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        // Usamos la URL de la imagen de la lista, es más pequeña
                        .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de ${species.name}",
                    modifier = Modifier.size(96.dp)
                )
                // Nombre
                Text(
                    text = species.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Si no es el último Pokémon, añade una flecha
            if (index < chain.size - 1) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Evoluciona a",
                    tint = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }

}
// --- ¡NUEVO COMPOSABLE PARA MOSTRAR DEBILIDADES/FORTALEZAS! ---
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TypeRelationsView(state: TypeRelationsState) {
    when (state) {
        is TypeRelationsState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }
        is TypeRelationsState.Success -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Débil Contra
                Text(
                    text = "Débil contra:",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    state.weakAgainst.forEach { typeName ->
                        TypeChip(type = typeName)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fuerte Contra
                Text(
                    text = "Fuerte contra:",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    state.strongAgainst.forEach { typeName ->
                        TypeChip(type = typeName)
                    }
                }
            }
        }
        is TypeRelationsState.Error -> {
            Text(text = state.message, color = MaterialTheme.colorScheme.error)
        }
    }
}