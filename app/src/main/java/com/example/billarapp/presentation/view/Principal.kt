package com.example.billarapp.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainComposable()
        }
    }
}

@Composable
fun MainComposable() {
    val currentScreen = remember { mutableStateOf("login") }

    when (currentScreen.value) {
        "login" -> LoginScreen(onRegisterClick = { currentScreen.value = "register" },
                onNavigateToBienvenida = { currentScreen.value = "bienvenida" })
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen.value = "login" })
        "bienvenida" -> BienvenidaScreen(
            onUnirseClick = { /* Navegar a Unirse a un Billar */ },
            onRegistrarClick = { currentScreen.value = "registro_billar" }
        )
        "registro_billar" -> RegistroBillarScreen(
            onNavigateToBienvenida = { currentScreen.value = "bienvenida" },
            onNavigateToAdmin = { currentScreen.value = "admin" }
        )
        "admin" -> AdminScreen(mesas = listOf("Mesa 1", "Mesa 2", "Mesa 3", "Mesa N"), // Lista de mesas
            onMesaClick = { mesaSeleccionada ->
                println("Mesa seleccionada: $mesaSeleccionada")
                // Agregar lógica para navegar o interactuar
            }
        )
    }
}