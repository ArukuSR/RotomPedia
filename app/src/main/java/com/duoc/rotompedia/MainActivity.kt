package com.duoc.rotompedia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.rotompedia.ui.theme.RotompediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RotompediaTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.rotomfondo),
                        contentDescription = "Fondo de la aplicación",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    Box(modifier = Modifier.fillMaxSize()){
                        IconButton(
                            onClick = {
                                Log.d("MainActivity", "Boton de login presionado")
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ){
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Login",
                                tint = Color.Black,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    MenuPrincipalScreen()
                }
            }
        }
    }
}

@Composable
fun MenuPrincipalScreen(pokemonViewModel: PokemonViewModel = viewModel()) {
    val apiState by pokemonViewModel.apiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp,  64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "RotomPedia",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Fila 1 Pokedex y Objetos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuButton(text = "Pokedex", modifier = Modifier.weight(1f))
            MenuButton(text = "Objetos", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Fila 2 Movimientos y Localizaciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuButton(text = "Movimientos", modifier = Modifier.weight(1f))
            MenuButton(text = "Localizaciones", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Resultado de la prueba de API
        ApiResultText(apiState = apiState)
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
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C4FAD)) // Azul
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ApiResultText(apiState: ApiState) {
    val (text, color) = when (apiState) {
        is ApiState.Loading -> "Cargando datos del primer Pokémon..." to Color.White
        is ApiState.Success -> apiState.info to Color(0xFFB9F6CA) // Verde claro para éxito
        is ApiState.Error -> apiState.message to Color(0xFFFF8A80) // Rojo claro para error
    }
    Text(
        text = text,
        color = color,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RotompediaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.rotomfondo),
                contentDescription = "Fondo de la aplicación",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Login",
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                }
                MenuPrincipalScreen()
            }
        }
    }
}