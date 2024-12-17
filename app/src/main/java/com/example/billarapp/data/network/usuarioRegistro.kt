package com.example.billarapp.data.network


import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.runBlocking

fun registrarUsuario(fullName: String, username: String, email: String, phone: String, password: String): Boolean {
    val client = supabaseBillar()
    return runBlocking {
        try {
            client.postgrest["usuarios"].insert(
                mapOf(
                    "full_name" to fullName,
                    "username" to username,
                    "email" to email,
                    "phone" to phone,
                    "password" to password
                )
            )
            true // Si no hay excepciones, la operación fue exitosa
        } catch (e: Exception) {
            e.printStackTrace()
            false // Si ocurre una excepción, la operación falló
        }
    }
}