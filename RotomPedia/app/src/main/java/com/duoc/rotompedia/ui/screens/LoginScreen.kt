package com.duoc.rotompedia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.rotompedia.R
import com.duoc.rotompedia.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onNavigateToRegister: () -> Unit
) {
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var recordarDatos by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.rotomfondo),
            contentDescription = "Fondo de la aplicación",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "RotomPedia",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF61DAF6),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Nombre de Usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF61DAF6),
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF61DAF6),
                    focusedContainerColor = Color(0x30FFFFFF),
                    unfocusedContainerColor = Color(0x30FFFFFF),
                    disabledContainerColor = Color(0x30FFFFFF),
                    focusedLabelColor = Color(0xFF61DAF6),
                    unfocusedLabelColor = Color.LightGray,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF61DAF6),
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF61DAF6),
                    focusedContainerColor = Color(0x30FFFFFF),
                    unfocusedContainerColor = Color(0x30FFFFFF),
                    disabledContainerColor = Color(0x30FFFFFF),
                    focusedLabelColor = Color(0xFF61DAF6),
                    unfocusedLabelColor = Color.LightGray,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = recordarDatos,
                    onCheckedChange = { recordarDatos = it }
                )
                Text(text = "Recordar datos", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { authViewModel.iniciarSesion(nombreUsuario, contrasena, recordarDatos) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF61DAF6), contentColor = Color.Black)
            ) {
                Text("Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ClickableText(
                text = AnnotatedString("¿No tienes cuenta? Regístrate"),
                onClick = { onNavigateToRegister() },
                style = TextStyle(color = Color.White, fontSize = 14.sp)
            )
        }
    }
}