package com.example.billarapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.billarapp.data.network.*

@Composable
fun RegistroBillarScreen(onNavigateToBienvenida: () -> Unit, onNavigateToAdmin: () -> Unit) {
    val nombreBillar = remember { mutableStateOf(TextFieldValue()) }
    val calle = remember { mutableStateOf(TextFieldValue()) }
    val colonia = remember { mutableStateOf(TextFieldValue()) }
    val alcaldiaMunicipio = remember { mutableStateOf(TextFieldValue()) }
    val estados = listOf("Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
        "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango", "Estado de México",
        "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit",
        "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
        "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán",
        "Zacatecas")
    val estadoSeleccionado = remember { mutableStateOf("") }
    val mostrarDialogo = remember { mutableStateOf(false) }
    val mensajeDialogo = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Agregar el scope de coroutines
    val coroutineScope = rememberCoroutineScope()

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
            Text(
                text = "Registrar Billar",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF7FD238),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            //Campos de Textos
            CustomTextField(
                value = nombreBillar.value,
                onValueChange = { nombreBillar.value = it },
                label = "Nombre del Billar"
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = calle.value,
                onValueChange = { calle.value = it },
                label = "Calle"
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = colonia.value,
                onValueChange = { colonia.value = it },
                label = "Colonia"
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = alcaldiaMunicipio.value,
                onValueChange = { alcaldiaMunicipio.value = it },
                label = "Alcaldía o Municipio"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Antes del DropdownMenu
            Text(
                text = if (estadoSeleccionado.value.isEmpty()) "Seleccionar Estado" else estadoSeleccionado.value,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E2C))
                    .padding(12.dp)
                    .clickable { expanded = true } // Cambia expanded a true al hacer clic
            )

            // DropdownMenu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color(0xFF1E1E2C))
            ) {
                estados.forEach { estado ->
                    DropdownMenuItem(
                        text = { Text(text = estado, color = Color.White) },
                        onClick = {
                            estadoSeleccionado.value = estado
                            expanded = false // Oculta el menú desplegable después de seleccionar
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onNavigateToBienvenida() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Regresar")
                }

                val context = LocalContext.current // Obtén el contexto actual

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val detalles = "${calle.value.text}, ${colonia.value.text}, ${alcaldiaMunicipio.value.text}, ${estadoSeleccionado.value}"
                                val codigo = generarCodigoBillar()

                                // Validar que los campos no estén vacíos
                                if (nombreBillar.value.text.isEmpty() || calle.value.text.isEmpty() ||
                                    colonia.value.text.isEmpty() || alcaldiaMunicipio.value.text.isEmpty() ||
                                    estadoSeleccionado.value.isEmpty()) {
                                    mensajeDialogo.value = "Por favor, complete todos los campos."
                                    mostrarDialogo.value = true
                                    return@launch
                                }

                                val registroExitoso = registrarBillar(nombreBillar.value.text, codigo, detalles)

                                if (registroExitoso) {
                                    mensajeDialogo.value = "Billar registrado correctamente"
                                    mostrarDialogo.value = true
                                } else {
                                    mensajeDialogo.value = "Error al registrar el billar en la base de datos"
                                    mostrarDialogo.value = true
                                }

                            } catch (e: Exception) {
                                mensajeDialogo.value = "Error inesperado: ${e.message}"
                            } finally {
                                mostrarDialogo.value = true
                            }
                        }

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99df5b))
                ) {
                    Text("Registrar")
                }
            }
        }
    }

    if (mostrarDialogo.value) {
        Dialog(onDismissRequest = { mostrarDialogo.value = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp),
                color = if (mensajeDialogo.value.contains("correctamente", true)) Color(0xff99df5b) else Color(0xFF1E1E2C)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = mensajeDialogo.value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (mensajeDialogo.value.contains("correctamente", true)) Color(0xFF0B0E1D) else Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            mostrarDialogo.value = false
                            if (mensajeDialogo.value.contains("correctamente", true)) {
                                onNavigateToAdmin() // Navega a Admin solo si el registro fue exitoso
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mensajeDialogo.value.contains("correctamente", true)) Color(0xFF99df5b) else Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, label: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF7FD238),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E2C), shape = MaterialTheme.shapes.small)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            decorationBox = { innerTextField ->
                Box(Modifier.fillMaxWidth()) {
                    if (value.text.isEmpty()) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}



fun generarCodigoBillar(): String {
    val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..8).map { caracteres.random() }.joinToString("")
}
