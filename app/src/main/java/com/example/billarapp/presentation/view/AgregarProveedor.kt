package com.example.billarapp.presentation.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billarapp.domain.ModeloProveedor
import com.example.billarapp.domain.agregarProveedorDB
import com.example.billarapp.domain.eliminarProveedor
import com.example.billarapp.domain.fetchProveedoresFromDatabase
import kotlinx.coroutines.launch

class AgregarProveedor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0E1D)
                ) {
                    PantallaAgregarProveedor(onBackClick = {
                        val activity = (LocalContext as? Activity)
                        activity?.finish()
                    })
                }
            }
        }
    }
}

@Composable
fun PantallaAgregarProveedor(onBackClick: () -> Unit) {
    var nombreProveedor by remember { mutableStateOf("") }
    var numeroTelefono by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    val proveedores = remember { mutableStateListOf<ModeloProveedor>() }
    val proveedoresSeleccionados = remember { mutableStateListOf<ModeloProveedor>() }
    val coroutineScope = rememberCoroutineScope()
    var cargando by remember { mutableStateOf(true) }
    var modoSeleccion by remember { mutableStateOf(false) }
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var mensajeExitoEliminacion by remember { mutableStateOf("") }
    var mostrarDialogoExitoEliminacion by remember { mutableStateOf(false) }
    var mensajeErrorEliminacion by remember { mutableStateOf("") }
    var mostrarDialogoErrorEliminacion by remember { mutableStateOf(false) }

    fun cargarProveedores() {
        coroutineScope.launch {
            try {
                val proveedoresDB = fetchProveedoresFromDatabase()
                proveedores.clear()
                proveedores.addAll(proveedoresDB)
            } catch (e: Exception) {
                mensajeError = "Error al cargar proveedores: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    if (cargando) {
        cargarProveedores()
    }

    // Diálogo de confirmación para agregar proveedor
    if (mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmacion = false },
            title = { Text("Confirmar") },
            text = { Text("¿Deseas agregar este proveedor?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoConfirmacion = false
                        coroutineScope.launch {
                            try {
                                val resultado = agregarProveedorDB(nombreProveedor, numeroTelefono)
                                if (resultado) {
                                    val nuevoProveedor = ModeloProveedor(
                                        id_proveedor = proveedores.size + 1,
                                        nombre = nombreProveedor,
                                        telefono = numeroTelefono
                                    )
                                    proveedores.add(nuevoProveedor)
                                    mostrarDialogoExito = true
                                    nombreProveedor = ""
                                    numeroTelefono = ""
                                }
                            } catch (e: Exception) {
                                mensajeError = "Error al agregar proveedor: ${e.message}"
                            }
                        }
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoConfirmacion = false }) {
                    Text("No")
                }
            }
        )
    }

    // Diálogo de éxito
    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoExito = false },
            title = { Text("Éxito") },
            text = { Text("Proveedor agregado con éxito") },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoExito = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Diálogo de confirmación para eliminar
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de eliminar ${proveedoresSeleccionados.size} proveedores?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                var eliminados = 0
                                proveedoresSeleccionados.forEach { proveedor ->
                                    val resultado = eliminarProveedor(proveedor)
                                    if (resultado) {
                                        proveedores.remove(proveedor)
                                        eliminados++
                                    }
                                }
                                if (eliminados > 0) {
                                    mensajeExitoEliminacion = if (eliminados == 1) {
                                        "Proveedor eliminado con éxito"
                                    } else {
                                        "$eliminados proveedores eliminados con éxito"
                                    }
                                    mostrarDialogoExitoEliminacion = true
                                }
                            } catch (e: Exception) {
                                // Maneja el error por relación con productos
                                mensajeErrorEliminacion = e.message ?: "Error desconocido"
                                mostrarDialogoErrorEliminacion = true
                            } finally {
                                proveedoresSeleccionados.clear()
                            }
                        }
                        mostrarDialogoEliminar = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("No")
                }
            }
        )
    }


// Diálogo de éxito al eliminar
    if (mostrarDialogoExitoEliminacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoExitoEliminacion = false },
            title = { Text("Éxito") },
            text = { Text(mensajeExitoEliminacion) },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoExitoEliminacion = false }) {
                    Text("OK")
                }
            }
        )
    }

// Diálogo de error al eliminar
    if (mostrarDialogoErrorEliminacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoErrorEliminacion = false },
            title = { Text("Error") },
            text = { Text(mensajeErrorEliminacion) },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoErrorEliminacion = false }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0E1D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón de regreso en la parte superior
            Button(
                onClick = { onBackClick() },
                modifier = Modifier.align(Alignment.Start),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff99df5b)
                )
            ) {
                Text("Regresar", color = Color(0xFF0B0E1D), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1D2B)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Añadir Proveedor",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff7FD238),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1D2B)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = nombreProveedor,
                        onValueChange = { nombreProveedor = it },
                        label = { Text("Nombre del Proveedor", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xff99df5b),
                            unfocusedBorderColor = Color(0xFFB2B6C1),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Persona",
                                tint = Color(0xff99df5b)
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = numeroTelefono,
                        onValueChange = { numeroTelefono = it },
                        label = { Text("Teléfono", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xff99df5b),
                            unfocusedBorderColor = Color(0xFFB2B6C1),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = "Teléfono",
                                tint = Color(0xff99df5b)
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (nombreProveedor.isBlank() || numeroTelefono.isBlank()) {
                                mensajeError = "Todos los campos son obligatorios."
                                return@Button
                            }
                            mostrarDialogoConfirmacion = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff99df5b)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Añadir Proveedor",
                            color = Color(0xFF0B0E1D),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            var textoSeleccionar by remember { mutableStateOf("Seleccionar todos") }

// Botones de selección
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón para habilitar/deshabilitar el modo selección
                Button(
                    onClick = { modoSeleccion = !modoSeleccion },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff99df5b)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(37.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Selección",
                        color = Color(0xFF0B0E1D),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón de seleccionar/deseleccionar todos
                if (modoSeleccion) {
                    Button(
                        onClick = {
                            if (proveedoresSeleccionados.size == proveedores.size) {
                                // Deseleccionar todos
                                proveedoresSeleccionados.clear()
                                textoSeleccionar = "Seleccionar todos"
                            } else {
                                // Seleccionar todos
                                proveedoresSeleccionados.clear()
                                proveedoresSeleccionados.addAll(proveedores)
                                textoSeleccionar = "Deseleccionar todos"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff7FD238)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            textoSeleccionar,
                            color = Color(0xFF0B0E1D),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Botón de eliminar seleccionados
                    Button(
                        onClick = {
                            if (proveedoresSeleccionados.isNotEmpty()) {
                                mostrarDialogoEliminar = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff7FD238)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Eliminar seleccionados",
                            color = Color(0xFF0B0E1D),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            // Lista de proveedores
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1D2B)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Proveedores Registrados",
                        color = Color(0xff7FD238),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Encabezados de la tabla
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (modoSeleccion) {
                            Spacer(modifier = Modifier.width(32.dp))
                        }
                        Text("ID", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("NOMBRE", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("TELÉFONO", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    if (cargando) {
                        Text(
                            "Cargando proveedores...",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else if (proveedores.isEmpty()) {
                        Text(
                            "No hay proveedores registrados.",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        LazyColumn {
                            items(proveedores) { proveedor ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (modoSeleccion) {
                                        Checkbox(
                                            checked = proveedoresSeleccionados.contains(proveedor),
                                            onCheckedChange = { isChecked ->
                                                if (isChecked) {
                                                    proveedoresSeleccionados.add(proveedor)
                                                } else {
                                                    proveedoresSeleccionados.remove(proveedor)
                                                }
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color(0xff7FD238),
                                                uncheckedColor = Color.White
                                            )
                                        )
                                    }
                                    Text(text = proveedor.id_proveedor.toString(), color = Color.White)
                                    Text(text = proveedor.nombre, color = Color.White)
                                    Text(text = proveedor.telefono, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}