package com.duoc.rotompedia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.rotompedia.R

@Composable
fun MapFeatureScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.rotomfondo),
            contentDescription = "Fondo de la aplicación",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Espaciador para bajar el contenido y alinearlo con el diseño del fondo
            Spacer(modifier = Modifier.height(280.dp))

            Text(
                text = "Selecciona una Región",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF61DAF6),
                textAlign = TextAlign.Center,
                // Se quita el padding superior, ahora controlado por el Spacer
                modifier = Modifier.padding(bottom = 24.dp)
            )
            RegionSelectionContent(navController = navController)
        }
    }
}

@Composable
fun RegionSelectionContent(navController: NavController) {
    val regions = listOf(
        "Kanto" to "kanto",
        "Johto" to "johto",
        "Hoenn" to "hoenn",
        "Sinnoh" to "sinnoh",
        "Unova" to "teselia",
        "Kalos" to "kalos",
        "Alola" to "alola",
        "Galar" to "galar",
        "Paldea" to "paldea"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(regions) { (displayName, internalName) ->
            MenuButton(
                text = displayName,
                onClick = { navController.navigate("map_viewer/$internalName") }
            )
        }
    }
}

@Composable
fun MapViewerScreen(region: String) {
    val context = LocalContext.current
    val resourceName = when (region) {
        "kanto" -> "kantomap"
        else -> "map_$region"
    }
    val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (resourceId != 0) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = "Mapa de $region",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = "Mapa de '$region' no encontrado.",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2A2A2A),
            contentColor = Color(0xFF61DAF6)
        )
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
