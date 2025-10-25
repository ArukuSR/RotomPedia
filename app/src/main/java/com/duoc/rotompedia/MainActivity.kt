package com.duoc.rotompedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.rotompedia.ui.theme.RotompediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RotompediaTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("menu_screen") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }
            )
        }
        composable("menu_screen") {
            MenuPrincipalScreen(
                onNavigateToPokedex = {
                    navController.navigate("pokedex_screen")
                }
            )
        }
        composable("pokedex_screen") {
            //la pantalla de la Pokedex
            PokedexScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate("pokemon_detail_screen/$pokemonId")
                }
            )
        }
        //NUEVA RUTA DE NAVEGACIÓN PARA LOS DETALLES
        composable(
            route = "pokemon_detail_screen/{pokemonId}",
            arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Obtenemos el ID de los argumentos de la ruta
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")
            if (pokemonId != null) {
                PokemonDetailScreen(pokemonId = pokemonId)
            }
        }
    }
}

@Composable
fun MenuPrincipalScreen(onNavigateToPokedex: () -> Unit) { // Ahora recibe una función para navegar
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.rotomfondo),
            contentDescription = "Fondo de la aplicación",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(33.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "RotomPedia",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF61DAF6),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 22.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Fila 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // El botón "Pokedex" ahora usa la función de navegación
                MenuButton(
                    text = "Pokedex",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToPokedex // <-- AQUI Hace la accion de navegar a la pagina donde esta la lista de la pokedex
                )
                MenuButton(text = "Objetos", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Fila 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuButton(text = "Movimientos", modifier = Modifier.weight(1f))
                MenuButton(text = "Localizaciones", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E7DBF))
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RotompediaTheme {
        MenuPrincipalScreen(onNavigateToPokedex = {})
    }
}