package com.example.billarapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.billarapp.data.network.registrarUsuario
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    // Estados para almacenar datos de entrada del formulario
    val fullName = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val confirmEmail = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val showMessage = remember { mutableStateOf(false) }
    val showError = remember { mutableStateOf(false) }
    val errors = remember { mutableStateOf(mapOf<String, String?>()) }

    // Muestra un cuadro de diálogo si el registro fue exitoso o fallido
    if (showMessage.value) {
        Dialog(onDismissRequest = { showMessage.value = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Usuario agregado correctamente")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            showMessage.value = false
                            onNavigateToLogin()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff99df5b),
                            contentColor = Color(0xFF0B0E1D)
                        )
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    } else if (showError.value) {
        Dialog(onDismissRequest = { showError.value = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error al registrar usuario")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            showError.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }

    // Fondo que abarca toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0E1D)) // Color de fondo para toda la pantalla
    ) {
        // Botón de regresar
        IconButton(
            onClick = { onNavigateToLogin() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = Color(0xFF7FD238)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Registro",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xff7FD238),
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Campos de texto con mensajes de error
            LabeledTextFieldWithError(
                value = fullName.value,
                onValueChange = { fullName.value = it },
                placeholder = "Nombre Completo",
                icon = Icons.Default.Person,
                error = errors.value["fullName"]
            )
            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextFieldWithError(
                value = username.value,
                onValueChange = { username.value = it },
                placeholder = "Nombre de Usuario",
                icon = Icons.Default.Person,
                error = errors.value["username"]
            )
            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextFieldWithError(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = "Correo",
                icon = Icons.Default.Email,
                error = errors.value["email"]
            )
            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextFieldWithError(
                value = confirmEmail.value,
                onValueChange = { confirmEmail.value = it },
                placeholder = "Confirmar Correo",
                icon = Icons.Default.Email,
                error = errors.value["confirmEmail"]
            )
            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextFieldWithError(
                value = phone.value,
                onValueChange = { phone.value = it },
                placeholder = "Celular",
                icon = Icons.Default.Phone,
                error = errors.value["phone"]
            )
            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextFieldWithError(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = "Contraseña",
                visualTransformation = PasswordVisualTransformation(),
                icon = Icons.Default.Lock,
                error = errors.value["password"]
            )
            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextFieldWithError(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                placeholder = "Confirmar Contraseña",
                visualTransformation = PasswordVisualTransformation(),
                icon = Icons.Default.Lock,
                error = errors.value["confirmPassword"]
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para registrar al usuario
            Button(
                onClick = {
                    val validationErrors = verifyRegistrationData(
                        fullName = fullName.value,
                        username = username.value,
                        email = email.value,
                        confirmEmail = confirmEmail.value,
                        phone = phone.value,
                        password = password.value,
                        confirmPassword = confirmPassword.value
                    )
                    errors.value = validationErrors
                    if (validationErrors.isEmpty()) {
                        val registroExitoso = registrarUsuario(
                            nombreCompleto = fullName.value,
                            nombreUsuario = username.value,
                            correo = email.value,
                            telefono = phone.value,
                            contraseña = password.value
                        )
                        if (registroExitoso) {
                            showMessage.value = true // Muestra mensaje de éxito
                        } else {
                            showError.value = true // Muestra mensaje de error
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff99df5b),
                    contentColor = Color(0xFF0B0E1D)
                )
            ) {
                Text("Registrar")
            }
        }
    }
}

@Composable
fun LabeledTextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    // Estado para mostrar/ocultar contraseña
    val isPasswordVisible = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E2C), MaterialTheme.shapes.small)
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ícono de la izquierda (candado, usuario, etc.)
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color(0xFF7FD238)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Campo de texto principal
                BasicTextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    modifier = Modifier
                        .weight(1f), // El campo ocupa el espacio restante
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    visualTransformation = if (visualTransformation == PasswordVisualTransformation() && !isPasswordVisible.value) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    decorationBox = { innerTextField ->
                        Box(Modifier.fillMaxWidth()) {
                            if (value.isEmpty()) {
                                Text(
                                    placeholder,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                // Ícono para mostrar/ocultar contraseña (solo si es campo de contraseña)
                if (visualTransformation == PasswordVisualTransformation()) {
                    Icon(
                        imageVector = if (isPasswordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible.value) "Ocultar contraseña" else "Mostrar contraseña",
                        modifier = Modifier
                            .size(24.dp) // Tamaño del ícono consistente
                            .clickable { isPasswordVisible.value = !isPasswordVisible.value }
                            .padding(start = 8.dp),
                        tint = Color(0xFF7FD238)
                    )
                }
            }
        }

        // Mensaje de error debajo del campo
        if (!error.isNullOrEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}


