package com.example.billarapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.graphics.BitmapFactory
import java.io.InputStream
import android.widget.Toast
import com.example.billarapp.data.network.verificarCredenciales


@Composable
fun LoginScreen(onRegisterClick: () -> Unit, onNavigateToBienvenida: () -> Unit) {
    // Estados para almacenar el usuario y contraseña
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isPasswordVisible = remember { mutableStateOf(false) }
    val context = LocalContext.current // Obtener el contexto

    // Cargar imagen desde assets
    val imageBitmap: ImageBitmap? = remember {
        val inputStream: InputStream = context.assets.open("logo_billar.png")
        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
    }

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
            // Mostrar la imagen en forma de círculo
            imageBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Logo Billar",
                    modifier = Modifier
                        .size(200.dp) // Ajusta el tamaño de la imagen
                        .clip(CircleShape) // Aplica la forma circular
                        .padding(bottom = 1.dp)
                )
            }
            /*Text(
                text = "Inicio de Sesion",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xff7FD238),
                modifier = Modifier.padding(bottom = 32.dp)
            )*/

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
                        .padding(10.dp),
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
                // Icono del candado a la izquierda (fuera del campo)
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono de candado",
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color(0xFF7FD238)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Campo de texto con el ícono de visibilidad dentro
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF1E1E2C), shape = MaterialTheme.shapes.small)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Campo de texto para la contraseña
                        BasicTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            modifier = Modifier.weight(1f),
                            visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                            decorationBox = { innerTextField ->
                                Box(Modifier.fillMaxWidth()) {
                                    if (password.value.isEmpty()) {
                                        Text(
                                            "Contraseña",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )

                        // Icono para mostrar/ocultar contraseña
                        Icon(
                            imageVector = if (isPasswordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible.value) "Ocultar contraseña" else "Mostrar contraseña",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isPasswordVisible.value = !isPasswordVisible.value },
                            tint = Color(0xFF7FD238)
                        )
                    }
                }
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
                onClick = {
                    val esValido = verificarCredenciales(context, username.value, password.value)
                    if (esValido) {
                        onNavigateToBienvenida() // Navega a la pantalla de bienvenida
                    } else {
                        Toast.makeText(context, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show()
                    } },
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
