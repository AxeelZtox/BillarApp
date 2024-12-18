package com.example.billarapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BienvenidaScreen(onUnirseClick: () -> Unit, onRegistrarClick: () -> Unit) {
    // Fondo de la pantalla
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
            // Título de la pantalla
            Text(
                text = "Bienvenido!",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF7FD238),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Pregunta encima de los botones
            Text(
                text = "¿Qué desea hacer?",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botón "Unirse a un Billar"
            Button(
                onClick = onUnirseClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF99df5b), // Color del botón
                    contentColor = Color(0xFF0B0E1D)   // Color del texto
                )
            ) {
                Text("Unirse a un Billar")
            }

            // Botón "Registrar Billar"
            Button(
                onClick = onRegistrarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF99df5b), // Color del botón
                    contentColor = Color(0xFF0B0E1D)   // Color del texto
                )
            ) {
                Text("Registrar Billar")
            }
        }
    }
}
