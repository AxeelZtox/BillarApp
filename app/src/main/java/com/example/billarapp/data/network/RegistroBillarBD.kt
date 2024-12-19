package com.example.billarapp.data.network

import android.util.Log
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.*

suspend fun registrarBillar(nombre: String, codigo: String, detalles: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val clienteSupabase = supabaseBillar()

            Log.d("RegistroBillar", "Intentando registrar: Nombre=$nombre, Código=$codigo, Detalles=$detalles")

            // Verifica si el código ya existe en la base de datos
            val response = clienteSupabase.postgrest
                .from("billar")
                .select() // Selecciona solo los códigos existentes

            val rawJson = response.data
            val jsonArray = Json.parseToJsonElement(rawJson).jsonArray

            val existeCodigo = jsonArray.any { jsonElement ->
                val jsonObject = jsonElement.jsonObject
                jsonObject["codigo_Billar"]?.jsonPrimitive?.content == codigo
            }

            if (existeCodigo) {
                Log.e("RegistroBillar", "El código ya existe: $codigo")
                return@withContext false
            }

            // Inserta el nuevo billar en la base de datos
            val nuevoBillar = mapOf(
                "nombre_billar" to nombre.trim(),
                "codigo_Billar" to codigo.trim(),
                "detalles_billar" to detalles.trim()
            )
            clienteSupabase.postgrest
                .from("billar")
                .insert(nuevoBillar)

            Log.i("RegistroBillar", "Billar registrado correctamente")
            true
        } catch (e: Exception) {
            Log.e("RegistroBillar", "Error al registrar billar: ${e.message}", e)
            false
        }
    }
}

suspend fun registrarBillarUsuario(codigo: String, rol: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val clienteSupabase = supabaseBillar()

            // Obtiene el ID del billar usando el código
            val response = clienteSupabase.postgrest
                .from("billar")
                .select() // Selecciona el ID y el código
            val rawJson = response.data
            val jsonArray = Json.parseToJsonElement(rawJson).jsonArray

            val idBillar = jsonArray.find { jsonElement ->
                val jsonObject = jsonElement.jsonObject
                jsonObject["codigo_Billar"]?.jsonPrimitive?.content == codigo
            }?.jsonObject?.get("id")?.jsonPrimitive?.content?.toIntOrNull()

            if (idBillar == null) {
                Log.e("RegistroBillarUsuario", "No se encontró el ID del billar para el código: $codigo")
                return@withContext false
            }

            // Inserta el registro en la tabla billar_usuario
            val fechaAsociacion = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val registroUsuario = mapOf(
                "id_billar" to idBillar,
                "fecha_asociacion" to fechaAsociacion,
                "rol" to rol
            )

            clienteSupabase.postgrest
                .from("billar_usuario")
                .insert(registroUsuario)

            Log.i("RegistroBillarUsuario", "Usuario asociado al billar correctamente")
            true
        } catch (e: Exception) {
            Log.e("RegistroBillarUsuario", "Error al asociar usuario al billar: ${e.message}", e)
            false
        }
    }
}

