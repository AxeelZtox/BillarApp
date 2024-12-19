package com.example.billarapp.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

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
    val currentScreen = remember { mutableStateOf("login")}

    when (currentScreen.value) {
        "login" -> LoginScreen(onRegisterClick = { currentScreen.value = "register" },
            onNavigateToBienvenida = { currentScreen.value = "bienvenida" },
            onNavigateToAdmin = { currentScreen.value = "admin" })

        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen.value = "login" })
        "bienvenida" -> BienvenidaScreen(
            onUnirseClick = { /* Navegar a Unirse a un Billar */ },
            onRegistrarClick = { currentScreen.value = "registro_billar" }
        )

        "registro_billar" -> RegistroBillarScreen(
            onNavigateToBienvenida = { currentScreen.value = "bienvenida" },
            onNavigateToMesas = { currentScreen.value = "add_mesas" }
        )

        "add_mesas" -> AddMesasScreen(
            id_billar = 3, // Reemplaza con el ID dinámico si es necesario
            onNavigateToAdmin = { currentScreen.value = "admin" }
        )

        "admin" -> AdminScreen(
            context = LocalContext.current,
            onProveedoresClick = { currentScreen.value = "agregar_proveedor" },
            onPoolClick = { currentScreen.value = "pool_mesas" },
            onCarambolaClick = { currentScreen.value = "carambola_mesas" },
            onCuentasClick = { currentScreen.value = "detalle_cuenta" },
            onMesaClick = { mesaSeleccionada ->
                currentScreen.value = "crear_cuenta_$mesaSeleccionada"
            }
        )

        "pool_mesas" -> AdminScreen(
            context = LocalContext.current,
            onProveedoresClick = { currentScreen.value = "agregar_proveedor" },
            onPoolClick = { /* Ya estamos en Pool */ },
            onCarambolaClick = { currentScreen.value = "carambola_mesas" },
            onCuentasClick = { currentScreen.value = "detalle_cuenta" },
            onMesaClick = { mesaSeleccionada ->
                currentScreen.value = "crear_cuenta_$mesaSeleccionada"
            }
        )

        "carambola_mesas" -> AdminScreen(
            context = LocalContext.current,
            onProveedoresClick = { currentScreen.value = "agregar_proveedor" },
            onPoolClick = { currentScreen.value = "pool_mesas" },
            onCarambolaClick = { /* Ya estamos en Carambola */ },
            onCuentasClick = { currentScreen.value = "detalle_cuenta" },
            onMesaClick = { mesaSeleccionada ->
                currentScreen.value = "crear_cuenta_$mesaSeleccionada"
            }
        )

        "agregar_proveedor" -> PantallaAgregarProveedor(
            onBackClick = { currentScreen.value = "admin" }
        )

        "detalle_cuenta" -> DetalleCuentaScreen(
            context = LocalContext.current,
            mesa = "1", // Cambia esto por el valor dinámico si es necesario
            cuenta = "123", // Cambia esto por el valor dinámico si es necesario
            cliente = "Cliente", // Cambia esto por el valor dinámico si es necesario
            tiempo = "1 hora 15 minutos", // Cambia esto por el valor dinámico si es necesario
            onBackClick = { currentScreen.value = "admin" }
        )

        else -> {
            val screen = currentScreen.value
            if (screen.startsWith("crear_cuenta_")) {
                val mesaSeleccionada = screen.removePrefix("crear_cuenta_").toIntOrNull() ?: 0
                CrearCuentaScreen(
                    id_billar = 3, // Ajusta el ID dinámico si es necesario
                    numero_mesa = mesaSeleccionada,
                    onBackClick = { currentScreen.value = "admin" }
                )
            }
        }

    }
}