package com.example.billarapp.domain

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.billarapp.data.network.supabaseBillar
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun getProductosFromDataBase(): SnapshotStateList<ProductoModel> {
    val productos = remember { mutableStateListOf<ProductoModel>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val data = supabaseBillar()
                    .postgrest
                    .from("Productos")
                    .select()
                    .decodeList<ProductoModel>()
                productos.addAll(data)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("dbg", "Error: ${e.message}")
            }
        }
    }
    return productos
}

suspend fun fetchProveedoresFromDatabase(): List<ModeloProveedor> {
    return try {
        val response = supabaseBillar()
            .postgrest
            .from("proveedor")
            .select()
        val rawJson = response.data ?: "Respuesta vacía"
        val jsonArray = Json.parseToJsonElement(rawJson).jsonArray
        jsonArray.map { jsonElement ->
            val jsonObject = jsonElement.jsonObject
            ModeloProveedor(
                id_proveedor = jsonObject["id_proveedor"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                nombre = jsonObject["nombre"]?.jsonPrimitive?.content ?: "",
                telefono = jsonObject["telefono"]?.jsonPrimitive?.content ?: ""
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}


suspend fun agregarProveedorDB(nombre: String, telefono: String): Boolean {
    return try {
        val clienteSupabase = supabaseBillar()

        val proveedor = mapOf(
            "nombre" to nombre.trim(),
            "telefono" to telefono.trim()
        )
        Log.d("ProveedorDebug", "Enviando proveedor: $proveedor")

        clienteSupabase
            .postgrest
            .from("proveedor")
            .insert(proveedor)

        Log.d("ProveedorDebug", "Inserción exitosa")
        true
    } catch (e: Exception) {
        Log.e("ErrorAgregarProveedor", "Error detallado: ", e)
        Log.e("ErrorAgregarProveedor", "Mensaje: ${e.message}")
        false
    }
}

suspend fun eliminarProveedor(proveedor: ModeloProveedor): Boolean {
    return try {
        val clienteSupabase = supabaseBillar()
        val response = clienteSupabase.postgrest
            .from(table = "proveedor")
            .delete {
                filter {
                    eq(column = "id_proveedor", proveedor.id_proveedor)
                }
            }
        response.data != null // Si la respuesta tiene datos, la operación fue exitosa
    } catch (e: Exception) {
        Log.e("ErrorEliminarProveedor", "Error detallado: ${e.message}")
        false
    }
}

