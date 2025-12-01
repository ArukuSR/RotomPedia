package com.duoc.rotompedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.duoc.rotompedia.data.model.GymLeader
import com.duoc.rotompedia.data.remote.GymApi
import com.duoc.rotompedia.util.getTipoColor
import com.duoc.rotompedia.util.traducirTipo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymLeadersScreen(
    navController: NavController
) {
    // Estado del Drawer (Menú lateral)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estado de los datos
    var selectedRegion by remember { mutableStateOf("Kanto") }
    var leadersList by remember { mutableStateOf<List<GymLeader>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Regiones disponibles en tu backend
    val regions = listOf("Kanto", "Johto", "Hoenn")

    // Cargar datos cuando cambia la región
    LaunchedEffect(selectedRegion) {
        isLoading = true
        try {
            // Llamada al Microservicio (Tu backend)
            leadersList = GymApi.service.getLeadersByRegion(selectedRegion)
        } catch (e: Exception) {
            leadersList = emptyList() // Manejo de error simple
        } finally {
            isLoading = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Selecciona Región",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                LazyColumn {
                    items(regions) { region ->
                        NavigationDrawerItem(
                            label = { Text(text = region) },
                            selected = region == selectedRegion,
                            onClick = {
                                selectedRegion = region
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Líderes: $selectedRegion",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFE3350D) // Rojo Pokédex
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Filtrar",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            containerColor = Color.White // Fondo blanco solicitado
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFE3350D)
                    )
                } else {
                    if (leadersList.isEmpty()) {
                        Text(
                            text = "No se encontraron líderes para $selectedRegion",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(leadersList) { leader ->
                                GymLeaderCard(leader)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GymLeaderCard(leader: GymLeader) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // Altura fija para uniformidad
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del Líder
            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa la mayor parte del espacio
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = leader.imageUrl.ifEmpty { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png" },
                    contentDescription = leader.name,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Datos del Líder
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = leader.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Chip de Tipo (Usamos tus utilidades existentes)
                Surface(
                    color = getTipoColor(leader.typeSpecialty), // Tu función de colores
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(24.dp)
                ) {
                    Text(
                        text = traducirTipo(leader.typeSpecialty), // Tu función de traducción
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Medalla: ${leader.badgeName}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}