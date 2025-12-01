package com.duoc.rotompedia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.duoc.rotompedia.viewmodel.allPokemonTypes
import com.duoc.rotompedia.viewmodel.PokemonViewModelFactory
import kotlinx.coroutines.launch
import com.duoc.rotompedia.util.traducirTipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    onPokemonClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModelFactory: PokemonViewModelFactory
) {
    val pokemonViewModel: PokemonViewModel = viewModel(factory = viewModelFactory)

    val state by pokemonViewModel.pokedexState.collectAsState()
    val selectedTypeFilter by pokemonViewModel.selectedTypeFilter.collectAsState()
    val searchQueryState by pokemonViewModel.searchQuery.collectAsState()
    val filteredList by pokemonViewModel.filteredPokemonList.collectAsState()
    val isFavoritesOnly by pokemonViewModel.isFavoritesOnly.collectAsState()
    val favoritePokemonIds by pokemonViewModel.favoritePokemonIds.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val regions = listOf("Todos", "Kanto", "Johto", "Hoenn", "Sinnoh", "Unova", "Kalos", "Alola", "Galar", "Paldea")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD11527),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
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
            OutlinedTextField(
                value = searchQueryState,
                onValueChange = {
                    pokemonViewModel.updateSearchQuery(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                label = { Text("Buscar Pokémon por nombre o ID") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true
            )

            // Filtro de Favoritos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = isFavoritesOnly,
                    onClick = { pokemonViewModel.toggleFavoritesOnly(!isFavoritesOnly) },
                    label = { Text("⭐ Favoritos") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD11527),
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    leadingIcon = if (isFavoritesOnly) {
                        {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Filtro Activo",
                                tint = Color.White
                            )
                        }
                    } else null
                )
            }

            // Filtros de Tipo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allPokemonTypes.forEach { typeName ->
                    FilterChip(
                        selected = typeName == selectedTypeFilter,
                        onClick = {
                            pokemonViewModel.updateTypeFilter(typeName)
                        },
                        label = {
                            Text(
                                traducirTipo(typeName),
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFD11527),
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is PokedexState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is PokedexState.Success -> {
                        if (filteredList.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = "No se encontraron Pokémon",
                                        fontSize = 18.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "Intenta con otro filtro o búsqueda",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredList) { pokemon ->
                                    val pokemonId = extractIdFromUrl(pokemon.url) ?: 0
                                    val pokemonIdStr = pokemonId.toString()

                                    PokemonListItemCard(
                                        pokemon = pokemon,
                                        pokemonId = pokemonIdStr,
                                        isFavorite = favoritePokemonIds.contains(pokemonId),
                                        onPokemonClick = onPokemonClick,
                                        onFavoriteToggle = {
                                            pokemonViewModel.toggleFavorite(
                                                pokemonId = pokemonId,
                                                pokemonName = pokemon.name,
                                                isCurrentlyFavorite = favoritePokemonIds.contains(pokemonId)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    is PokedexState.Error -> {
                        Text(
                            text = (state as PokedexState.Error).message,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState
            ) {
                FilterSheetContent(
                    regions = regions,
                    selectedType = selectedTypeFilter,
                    isFavoritesOnly = isFavoritesOnly,
                    onRegionSelected = { regionName ->
                        pokemonViewModel.updateRegionFilter(regionName)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showFilterSheet = false
                            }
                        }
                    },
                    onTypeSelected = { typeName ->
                        pokemonViewModel.updateTypeFilter(typeName)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showFilterSheet = false
                            }
                        }
                    },
                    onFavoritesToggle = { isEnabled ->
                        pokemonViewModel.toggleFavoritesOnly(isEnabled)
                    },
                    onClearFilters = {
                        pokemonViewModel.clearAllFilters()
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
fun PokemonListItemCard(
    pokemon: PokemonListItem,
    pokemonId: String,
    isFavorite: Boolean,
    onPokemonClick: (String) -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPokemonClick(pokemonId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                color = Color.Gray,
                modifier = Modifier.width(60.dp)
            )

            Text(
                text = pokemon.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { onFavoriteToggle() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Eliminar de favoritos" else "Añadir a favoritos",
                    tint = if (isFavorite) Color(0xFFFFD700) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun FilterSheetContent(
    regions: List<String>,
    selectedType: String,
    isFavoritesOnly: Boolean,
    onRegionSelected: (String) -> Unit,
    onTypeSelected: (String) -> Unit,
    onFavoritesToggle: (Boolean) -> Unit,
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filtrar Pokémon",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onClearFilters) {
                Text("Limpiar")
            }
        }

        // Sección de Favoritos
        Text(
            text = "Favoritos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.SemiBold
        )

        ListItem(
            headlineContent = { Text("Mostrar Solo Favoritos") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onFavoritesToggle(!isFavoritesOnly) }
                .padding(horizontal = 8.dp),
            trailingContent = {
                Switch(
                    checked = isFavoritesOnly,
                    onCheckedChange = { onFavoritesToggle(it) }
                )
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = if (isFavoritesOnly) Color(0xFFD11527) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Text(
            text = "Por Tipo",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.SemiBold
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(allPokemonTypes.chunked(3)) { rowTypes ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowTypes.forEach { typeName ->
                        FilterChip(
                            selected = typeName == selectedType,
                            onClick = { onTypeSelected(typeName) },
                            label = {
                                Text(
                                    traducirTipo(typeName),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFD11527),
                                selectedLabelColor = Color.White,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(3 - rowTypes.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Text(
            text = "Por Región",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.SemiBold
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            items(regions) { region ->
                ListItem(
                    headlineContent = {
                        Text(
                            region,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRegionSelected(region) }
                        .padding(horizontal = 8.dp),
                    leadingContent = {
                        if (region != "Todos") {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

private fun extractIdFromUrl(url: String): Int? {
    return try {
        url.split("/")
            .dropLast(1)
            .last()
            .toIntOrNull()
    } catch (e: Exception) {
        null
    }
}