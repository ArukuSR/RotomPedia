package com.duoc.rotompedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.rotompedia.data.TIPO_NAMES_EN
import com.duoc.rotompedia.data.TYPE_CHART_MATRIX
import com.duoc.rotompedia.util.getTipoColor
import com.duoc.rotompedia.util.traducirTipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeChartScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabla de Tipos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD11527),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
                .padding(8.dp) // Un padding general
        ) {
            Column {
                // Fila de Encabezado (Tipos Defensores)
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // Celda de la esquina con el texto que pediste
                    HeaderCell(
                        text = "Defensa →\nAtaque ↓",
                        isColumnHeader = true
                    )

                    TIPO_NAMES_EN.forEach { tipo ->
                        HeaderCell(text = traducirTipo(tipo), typeColor = getTipoColor(tipo))
                    }
                }

                // Filas de la Matriz (Atacantes)
                TIPO_NAMES_EN.forEachIndexed { rowIndex, tipoAtacante ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HeaderCell(
                            text = traducirTipo(tipoAtacante),
                            typeColor = getTipoColor(tipoAtacante),
                            isColumnHeader = true
                        )

                        TYPE_CHART_MATRIX[rowIndex].forEach { efectividad ->
                            EffectivenessCell(value = efectividad)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Celda de encabezado (para el nombre del tipo).
 */
@Composable
fun HeaderCell(text: String, typeColor: Color = MaterialTheme.colorScheme.surfaceVariant, isColumnHeader: Boolean = false) {

    // --- LÓGICA DE TAMAÑO DE TEXTO ---
    // Si es una celda de encabezado Y tiene un salto de línea (es la esquina),
    // achicamos el texto para que quepa en el tamaño estándar.
    val isCornerCell = isColumnHeader && text.contains("\n")
    val fontSize = if (isCornerCell) 11.sp else 13.sp
    // ---

    Box(
        modifier = Modifier
            .size(width = 75.dp, height = 50.dp) // Tamaño estándar equitativo
            .padding(1.dp) // Pequeño espacio entre celdas
            .clip(RoundedCornerShape(8.dp)) // Bordes redondeados
            .background(typeColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize, // Tamaño de fuente dinámico
            fontWeight = FontWeight.Bold,
            color = if (typeColor == MaterialTheme.colorScheme.surfaceVariant) Color.Black else Color.White,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Celda para los valores (2x, ½, 0).
 */
@Composable
fun EffectivenessCell(value: String) {
    val (text, bgColor) = when (value) {
        "2" -> "2x" to Color(0xFF4CAF50).copy(alpha = 0.7f) // Verde
        "0.5" -> "½" to Color(0xFFF44336).copy(alpha = 0.7f) // Rojo
        "0" -> "0" to Color(0xFFBDBDBD).copy(alpha = 0.7f) // Gris
        else -> "" to MaterialTheme.colorScheme.background.copy(alpha = 0.0f) // Transparente
    }

    Box(
        modifier = Modifier
            .size(width = 75.dp, height = 50.dp) // Mismo tamaño estándar
            .padding(1.dp)
            .clip(RoundedCornerShape(8.dp)) // Mismos bordes redondeados
            .background(bgColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}