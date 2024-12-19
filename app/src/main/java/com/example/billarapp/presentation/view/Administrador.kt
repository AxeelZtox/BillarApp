package com.example.billarapp.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.layout.imePadding

@Composable
fun AdminScreen() {
    MaterialTheme {
        MultiplicationApp()
    }
}

@Composable
fun MultiplicationApp() {
    // Variables de estado para almacenar los números y el resultado
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Float?>(null) }
    var showCloseButton by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") } // Estado para manejar mensajes de error

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .imePadding() // Asegura que el contenido se ajuste al teclado
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título de la aplicación
            Text(
                text = "App para Multiplicar",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Inputs para ingresar los números (solo numéricos)
            TextField(
                value = number1,
                onValueChange = {
                    number1 = it
                    errorMessage = "" // Limpiar mensaje de error al modificar entrada
                },
                label = { Text("Número 1") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre campos

            TextField(
                value = number2,
                onValueChange = {
                    number2 = it
                    errorMessage = "" // Limpiar mensaje de error al modificar entrada
                },
                label = { Text("Número 2") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp)) // Espaciado entre el último campo y el botón

            // Botón para realizar la multiplicación
            Button(onClick = {
                val num1 = number1.toFloatOrNull()
                val num2 = number2.toFloatOrNull()
                if (num1 != null && num2 != null) {
                    result = num1 * num2
                    showCloseButton = (result ?: 0f) < 0f
                    errorMessage = "" // Limpiar mensaje de error en caso de éxito
                    keyboardController?.hide() // Ocultar teclado al realizar la operación
                } else {
                    errorMessage = "Ingrese valores válidos" // Actualizar mensaje de error
                }
            }) {
                Text("Multiplicar")
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre el botón y el resultado

            // Mostrar mensaje de error si existe
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = androidx.compose.ui.graphics.Color.Red)
            }

            // Mostrar el resultado de la multiplicación o el mensaje "OK" si es positivo
            result?.let { res ->
                if (res > 0f) {
                    Text("Resultado: OK ($res)")
                } else {
                    Text("Resultado: $res")
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre el resultado y el botón cerrar

            // Botón "Cerrar" que se muestra solo si el resultado es negativo o cero
            AnimatedVisibility(visible = showCloseButton) {
                Button(onClick = {
                    // Reiniciar la app
                    number1 = ""
                    number2 = ""
                    result = null
                    showCloseButton = false
                    errorMessage = ""
                }) {
                    Text("Cerrar")
                }
            }
        }
    }
}
