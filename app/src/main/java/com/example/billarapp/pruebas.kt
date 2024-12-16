package com.example.billarapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.billarapp.data.network.supabaseBillar
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class pruebas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pruebas)

        val connectionStatusText = findViewById<TextView>(R.id.connectionStatusText)
        fetchProductById(connectionStatusText,3)
    }

    private fun fetchProductById(connectionStatusText: TextView, productId: Int) {
        lifecycleScope.launch {
            try {
                // Filtrar la consulta por el ID del producto
                /*val supabaseResponse = supabaseBillar
                    .postgrest["TablaPruebas"]
                    .select {
                        "id = $productId" // Filtro manual

                    }*/

                val supabaseResponse = supabaseBillar().from("TablaPruebas").select(columns = Columns.list("id, nombre")){
                    filter {
                        TablaPruebas::id eq "2"
                    }
                }

                val data = supabaseResponse.decodeList<TablaPruebas>()

                // Si los datos están disponibles, actualiza el TextView
                if (data.isNotEmpty()) {
                    val producto = data.first() // Obtén el producto que coincide con el filtro
                    connectionStatusText.text = "Producto: ${producto.nombre}"
                } else {
                    connectionStatusText.text = "Producto con ID $productId no encontrado"
                }
            } catch (e: Exception) {
                // Manejo de errores
                connectionStatusText.text = "Error al cargar el producto"
                Log.e("Supabase", "Error: ${e.message}")
            }
        }
    }
} 

@Serializable
data class TablaPruebas(
    val id: Int = 0,
    val nombre: String =""
)
