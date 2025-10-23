package com.duoc.rotompedia

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    pokemonViewModel: PokemonViewModel = viewModel(),
    onPokemonClick: (String) -> Unit
) {
    val state by pokemonViewModel.pokedexState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex Nacional") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD11527),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state) {
                is PokedexState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PokedexState.Success -> {
                    PokemonList(
                        pokemonList = currentState.pokemonList,
                        onPokemonClick = onPokemonClick
                    )
                }
                is PokedexState.Error -> {
                    Text(
                        text = currentState.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonListItem>,
    onPokemonClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(pokemonList) { pokemon ->
            //Extraemos el ID del Pokémon de su URL para construir la URL de la imagen
            val pokemonId = pokemon.url.split("/").dropLast(1).last()
            PokemonRow(
                pokemon = pokemon,
                pokemonId = pokemonId,
                onPokemonClick = onPokemonClick //Pasamos la función de clic
            )
        }
    }
}

@Composable
fun PokemonRow(
    pokemon: PokemonListItem,
    pokemonId: String,
    onPokemonClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            //Hacemos que toda la tarjeta sea clicable
            .clickable { onPokemonClick(pokemonId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //Usamos AsyncImage de Coil para cargar la imagen desde la URL
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    // URL de alta calidad para las imágenes de los Pokémon
                    .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png")
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de ${pokemon.name}",
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = "#$pokemonId",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                text = pokemon.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}