package com.duoc.rotompedia.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.rotompedia.R
import com.duoc.rotompedia.ui.theme.RotompediaTheme
import com.duoc.rotompedia.viewmodel.AuthState
import com.duoc.rotompedia.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.REGISTRATION_SUCCESS -> {
                Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                authViewModel.resetAuthState()
                onNavigateToLogin()
            }
            AuthState.USER_EXISTS -> {
                Toast.makeText(context, "El nombre de usuario ya está en uso", Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }
            AuthState.EMAIL_EXISTS -> {
                Toast.makeText(context, "El correo electrónico ya está registrado", Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }
            AuthState.EMPTY_FIELDS -> {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.rotomfondo),
            contentDescription = "Fondo de Alola",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de Usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    authViewModel.registrarUsuario(nombre, correo, contrasena)
                },
                enabled = authState != AuthState.LOADING,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (authState == AuthState.LOADING) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrarse")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes una cuenta? Inicia Sesión")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RotompediaTheme {
        RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {})
    }
}
