package com.example.billarapp.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breens.beetablescompose.BeeTablesCompose
import com.example.billarapp.domain.getProductosFromDataBase

class Productos : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShowContent()
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ShowContent(){
    ProductosScreen()
}


@Composable
fun ProductosScreen() {
    /*val productos = remember { mutableStateListOf<ProductoModel>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val data = supabaseBillar()
                    .from("Productos")
                    .select()
                    .decodeList<ProductoModel>()
                productos.addAll(data)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("dbg", "Error: ${e.message}")
            }
        }
    }*/

    val productos = getProductosFromDataBase()

    val tableHeaders = listOf("Id prod.", "Id prov.", "Producto", "Precio", "Stock")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color(0xFFB7BABC)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Acción para volver atrás */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.width(70.dp)
                ) {
                    Text("Back")
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* Acción para editar producto */ }) {
                    Text("Editar")
                }
                Button(onClick = { /* Acción para eliminar producto */ }) {
                    Text("Eliminar")
                }
                Button(onClick = { /* Acción para añadir producto */ }) {
                    Text("Añadir producto")
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCD6D))
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Productos",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B0E1D),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lista de productos",
                    fontSize = 30.sp,
                    color = Color(0xFF4A4D5D),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                BeeTablesCompose(data = productos, headerTableTitles = tableHeaders)
            }
        }
    )
}




