package com.example.billarapp.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billarapp.domain.ProductoModel
import com.example.billarapp.domain.eliminarProducto
import com.example.billarapp.domain.getProductosFromDataBase
import com.example.billarapp.ui.theme.SystemBarsColorChanger

import kotlinx.coroutines.launch

class Productos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent{

            MaterialTheme{
                SystemBarsColorChanger(
                    statusBarColor = Color.Black,
                    navigationBarColor = Color.Black,
                    isLightIcons = true
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0E1D)
                ){
                    ShowContent()
                }
            }
        }

    }

}






@Preview(showBackground = true)
@Composable
private fun ShowContent(){
    ProductosScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductosScreen() {
    val productos = remember { mutableStateListOf<ProductoModel>() }
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var cargando by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var mensajeError by remember { mutableStateOf("") }
    val activity = (LocalContext.current as? Activity)


    fun cargarProductos() {
        coroutineScope.launch {
            try {
                val productoDB = getProductosFromDataBase()
                productos.clear()
                productos.addAll(productoDB)
            } catch (e: Exception) {
                mensajeError = "Error al cargar proveedores: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    if (cargando) {
        cargarProductos()
    }

    Scaffold(
        //TopBar de la pantalla, incluyendo colores definidos, boton de regresar y titulo
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1D2B),
                    titleContentColor = Color(0xFFffffff),
                    navigationIconContentColor = Color(0xFFFFFFFF)
                ),
                title = {
                    Text(
                        "Productos",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },


                navigationIcon = {
                    /*CORREGIR CLASE LLAMADA EN LA IMPLEMENTACION DE LAS DEMAS PANTALLAS*/
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },

        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF1A1D2B),
                contentColor = Color(0xFFFFFFFF),
                actions = {
                    //Ese boton debe llamar al menu principal
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Localized description")
                    }

                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(context, AnadirProd::class.java)
                            context.startActivity(intent)
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),

                        ) {
                        Icon(Icons.Filled.Add, "Add products")
                    }

                },

                )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0E1D))
                    .padding(innerPadding)
                    .padding(18.dp)
            ) {
                var modoSeleccion by remember { mutableStateOf(false) }
                var textoSeleccionar by remember { mutableStateOf("Todos") }
                val productosSeleccionados = remember { mutableStateListOf<ProductoModel>() }
                var mostrarDialogoEliminar by remember { mutableStateOf(false) }
                var mostrarDialogoExito by remember { mutableStateOf(false) }

                // Diálogo de confirmación para eliminar
                if (mostrarDialogoEliminar) {
                    AlertDialog(
                        onDismissRequest = { mostrarDialogoEliminar = false },
                        title = { Text("Confirmar eliminación") },
                        text = { Text("¿Estás seguro de eliminar ${productosSeleccionados.size} producto(s)?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        productosSeleccionados.forEach { producto ->
                                            val resultado = eliminarProducto(producto)
                                            if (resultado) {
                                                productos.remove(producto)
                                            }
                                        }
                                        productosSeleccionados.clear()
                                    }
                                    mostrarDialogoEliminar = false
                                    mostrarDialogoExito = true
                                }
                            ) {
                                Text("Sí")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { mostrarDialogoEliminar = false }) {
                                Text("No")
                            }
                        }
                    )
                }

                if (mostrarDialogoExito) {
                    AlertDialog(
                        onDismissRequest = { mostrarDialogoExito = false },
                        title = { Text("Productos eliminados") },
                        text = { Text("Los productos seleccionados han sido eliminados exitosamente.") },
                        confirmButton = {
                            TextButton(onClick = { mostrarDialogoExito = false }) {
                                Text("Aceptar")
                            }
                        }
                    )
                }



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón para habilitar/deshabilitar el modo selección
                    Button(
                        onClick = { modoSeleccion = !modoSeleccion },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff99df5b)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(37.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if(!modoSeleccion){
                            Row{
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Editar",
                                    tint = Color(0xFF0B0E1D),
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                )
                                Text(
                                    "Editar",
                                    color = Color(0xFF0B0E1D),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                "",
                                color = Color(0xFF0B0E1D),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }else{
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Cerrar modo selección",
                                tint = Color(0xFF0B0E1D)
                            )
                        }
                    }

                    if (modoSeleccion) {

                        // Botón de seleccionar/deseleccionar todos
                        Button(
                            onClick = {
                                if (productosSeleccionados.size == productos.size) {
                                    // Deseleccionar todos
                                    productosSeleccionados.clear()
                                    textoSeleccionar = "Todos"
                                } else {
                                    // Seleccionar todos
                                    productosSeleccionados.clear()
                                    productosSeleccionados.addAll(productos)
                                    textoSeleccionar = "Limpiar selecc."
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff7FD238)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                textoSeleccionar,
                                color = Color(0xFF0B0E1D),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        //Boton editar
                        if(productosSeleccionados.size!=1){
                            Button(
                                onClick = {/*nohacenada*/},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFB2B6C1),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp)

                            ) {
                                Row{
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar",
                                        modifier = Modifier
                                            .padding(end = 5.dp)
                                    )
                                }
                            }
                        }else{
                            Button(
                            onClick = {               /*Cambiar actividad a Editar producto*/
                                val intent = Intent(context, EditarProducto::class.java).apply {
                                    putExtra("producto",productosSeleccionados.first())
                                }
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff7FD238),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)

                        ) {
                            Row{
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Editar",
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                )
                            }
                        }

                        }

                        // Botón de eliminar seleccionados
                        Button(
                            onClick = {
                                if (productosSeleccionados.isNotEmpty()) {
                                    Log.i("dbg","Productos ${productosSeleccionados.size}")
                                    mostrarDialogoEliminar = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff7FD238)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Cerrar modo selección",
                                tint = Color(0xFF0B0E1D)
                            )
                        }
                    }
                }


                // Lista de productos
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1D2B)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Productos Registrados",
                            color = Color(0xff7FD238),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Encabezados de la tabla
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (modoSeleccion) {
                                Spacer(modifier = Modifier.width(32.dp))
                            }
                            Text("ID", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("# PROV", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("PRODUCTO", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("$", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Cant.", color = Color.White, fontWeight = FontWeight.Bold)

                        }

                        if (cargando) {
                            Text(
                                "Cargando productos...",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else if (productos.isEmpty()) {
                            Text(
                                "No hay proveedores registrados.",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            LazyColumn {
                                items(productos) { producto ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (modoSeleccion) {
                                            Checkbox(
                                                checked = productosSeleccionados.contains(producto),
                                                onCheckedChange = { isChecked ->
                                                    if (isChecked) {
                                                        productosSeleccionados.add(producto)
                                                    } else {
                                                        productosSeleccionados.remove(producto)
                                                    }
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Color(0xff7FD238),
                                                    uncheckedColor = Color.White
                                                )
                                            )
                                        }
                                        Text(
                                            text = producto.id_producto.toString(),
                                            color = Color.White
                                        )
                                        Text(
                                            text = producto.id_proveedor.toString(),
                                            color = Color.White
                                        )
                                        Text(text = producto.det_producto, color = Color.White)
                                        Text(text = producto.precio.toString(), color = Color.White)
                                        Text(
                                            text = producto.Cantidad_Inv.toString(),
                                            color = Color.White
                                        )

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    )
}








