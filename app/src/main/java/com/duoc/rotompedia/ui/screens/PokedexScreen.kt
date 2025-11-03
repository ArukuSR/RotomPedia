// PokedexScreen.kt
package com.duoc.rotompedia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.duoc.rotompedia.data.model.PokemonListItem
import com.duoc.rotompedia.viewmodel.PokedexState
import com.duoc.rotompedia.viewmodel.PokemonViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    pokemonViewModel: PokemonViewModel = viewModel(),
    onPokemonClick: (String) -> Unit,
    onNavigateBack: () -> Unit // <-- NUEVO PARÁMETRO
) {
    val state by pokemonViewModel.pokedexState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // --- NUEVO: Estados para el menú deslizable ---
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val regions = listOf("Todos", "Kanto", "Johto", "Hoenn", "Sinnoh", "Unova", "Kalos", "Alola", "Galar", "Paldea")
    // ---

    Scaffold(
        topBar = {
            TopAppBar(
                // Cambiamos el título como en tu imagen
                title = { Text("Pokédex") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD11527),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                // --- ¡NUEVO: Botón de Volver! ---
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                // --- ¡NUEVO: Botón de Filtro! ---
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // --- Barra de Búsqueda (AHORA FUNCIONAL) ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    // Llamamos al ViewModel para que filtre EN TIEMPO REAL
                    pokemonViewModel.filterPokemonListBySearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                label = { Text("Buscar Pokémon por nombre o ID") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true
            )

            // --- Lista de Pokémon ---
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (val currentState = state) {
                    is PokedexState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is PokedexState.Success -> {
                        PokemonList(
                            // Pasamos la LISTA FILTRADA
                            pokemonList = currentState.filteredPokemonList,
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

        // --- ¡NUEVO: Menú Deslizable (ModalBottomSheet)! ---
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState
            ) {
                // Contenido del menú
                FilterSheetContent(
                    regions = regions,
                    onRegionSelected = { regionName ->
                        // 1. Llamamos al ViewModel para filtrar por región
                        pokemonViewModel.filterPokemonListByRegion(regionName)

                        // 2. Reseteamos la búsqueda de texto
                        searchQuery = ""

                        // 3. Cerramos el menú
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showFilterSheet = false
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonListItem>,
    onPokemonClick: (String) -> Unit
) {
    // Si la lista filtrada está vacía, muestra un mensaje
    if (pokemonList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No se encontraron Pokémon",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(pokemonList) { pokemon ->
                val pokemonId = pokemon.url.split("/").dropLast(1).last()
                PokemonListItemCard(
                    pokemon = pokemon,
                    pokemonId = pokemonId,
                    onPokemonClick = onPokemonClick
                )
            }
        }
    }
}

// ... (PokemonListItemCard no cambia) ...
@Composable
fun PokemonListItemCard(
    pokemon: PokemonListItem,
    pokemonId: String,
    onPokemonClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPokemonClick(pokemonId) }, // Hacemos clicable
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
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
                text = pokemon.name, // El nombre ya viene capitalizado desde el ViewModel
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// --- ¡NUEVO: Composable para el contenido del menú de filtro! ---
@Composable
fun FilterSheetContent(
    regions: List<String>,
    onRegionSelected: (String) -> Unit
) {
    Column(Modifier.padding(bottom = 32.dp)) {
        Text(
            text = "Filtrar por Región",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(regions) { region ->
                ListItem(
                    headlineContent = { Text(region) },
                    modifier = Modifier.clickable { onRegionSelected(region) }
                )
            }
        }
    }
}