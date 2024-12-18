package com.example.billarapp.data.network

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.runBlocking

fun registrarUsuario(nombreCompleto: String, nombreUsuario: String, correo: String, telefono: String, contraseña: String): Boolean {
    val client = supabaseBillar() // Verifica que esta función esté configurada correctamente
    return runBlocking {
        try {
            // Inserta los datos en la tabla "usuarios"
            client.postgrest["datos_usuario"].insert(
                mapOf(
                    "nombre_completo" to nombreCompleto,
                    "nombre_usuario" to nombreUsuario,
                    "correo" to correo,
                    "telefono" to telefono,
                    "contraseña" to contraseña
                )
            )
            true // Operación exitosa
        } catch (e: Exception) {
            e.printStackTrace()
            false // Fallo en la operación
        }
    }
}
