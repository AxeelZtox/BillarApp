package com.example.billarapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*


@Composable
fun LoginScreen(onRegisterClick: () -> Unit) {
    // Estados para almacenar el usuario y contraseña
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current // Obtener el contexto

    // Diseño de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0E1D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostrar la imagen
            Image(
                bitmap = imageBitmap,
                contentDescription = "Logo Billar",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Inicio de Sesion",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xff7FD238),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de texto para el usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icono de usuario",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF7FD238) // Color del ícono
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E2C), shape = MaterialTheme.shapes.small) // Fondo del campo
                        .padding(8.dp),
                    decorationBox = { innerTextField ->
                        Box(Modifier.fillMaxWidth()) {
                            if (username.value.isEmpty()) {
                                Text(
                                    "Usuario",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray // Color del texto de marcador
                                )
                            }
                            innerTextField()
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White) // Color del texto ingresado
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para la contraseña
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono de candado",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF7FD238) // Color del ícono
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E2C), shape = MaterialTheme.shapes.small) // Fondo del campo
                        .padding(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    decorationBox = { innerTextField ->
                        Box(Modifier.fillMaxWidth()) {
                            if (password.value.isEmpty()) {
                                Text(
                                    "Contraseña",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray // Color del texto de marcador
                                )
                            }
                            innerTextField()
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White) // Color del texto ingresado
                )
            }

            // Botón interactuable "Olvidaste contraseña"
            TextButton(
                onClick = { olvideContraseña(context) }, // Método que se ejecutará al hacer clic
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7FD238)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Login
            Button(
                onClick = { /* Acción de login */ },
                modifier = Modifier.height(40.dp).width(250.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff99df5b), // Fondo del botón
                    contentColor = Color(0xFF0B0E1D)   // Color del texto
                )
            ) {
                Text("Iniciar Sesion")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para registrar un nuevo usuario
            Button(
                onClick = { onRegisterClick() },
                modifier = Modifier.height(40.dp).width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff99df5b), // Fondo del botón
                    contentColor = Color(0xFF0B0E1D)   // Color del texto
                )
            ) {
                Text("Registrar")
            }
        }
    }
}
