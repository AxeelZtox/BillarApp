package com.example.billarapp.data.network

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun verificarCredenciales(username: String, password: String): Boolean {
    return runBlocking {
        try {
            val response = supabaseBillar()
                .postgrest
                .from("datos_usuario")
                .select()

            val rawJson = response.data// Asegura que siempre haya algo para parsear
            val jsonArray = Json.parseToJsonElement(rawJson).jsonArray

            val usuarioValido = jsonArray.any { jsonElement ->
                val jsonObject = jsonElement.jsonObject
                val dbUsername = jsonObject["nombre_usuario"]?.jsonPrimitive?.content
                val dbPassword = jsonObject["contraseña"]?.jsonPrimitive?.content
                dbUsername == username && dbPassword == password
            }

            println("Usuario válido: $usuarioValido")
            usuarioValido
        } catch (e: Exception) {
            println("Error al verificar usuario: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
