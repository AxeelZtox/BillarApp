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
        "login" -> LoginScreen(onRegisterClick = { currentScreen.value = "register" })
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen.value = "login" })
    }
}
