package com.example.billarapp.presentation.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.billarapp.domain.actualizarFechaCierreCuenta
import com.example.billarapp.ui.theme.SystemBarsColorChanger
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DetalleMesa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val idCuenta = intent.getIntExtra("id_cuenta", 0)
        val numeroMesa = intent.getIntExtra("numero_mesa", 0)
        val tipoMesa = intent.getStringExtra("tipo_mesa") ?: ""*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
            )
        }

        setContent {
            MaterialTheme {
                SystemBarsColorChanger(
                    statusBarColor = Color.Black,
                    navigationBarColor = Color.Black,
                    isLightIcons = true
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0E1D)
                ) {
                    DetalleMesaScreen(2, 2, "POOL")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMesaScreen(idCuenta: Int, numeroMesa: Int, tipoMesa: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Estados
    var tiempoTranscurrido by remember { mutableStateOf("0 minutos") }
    var costoTiempo by remember { mutableStateOf(0.0) }
    var clienteNombre by remember { mutableStateOf("Cliente") }
    var productos by remember { mutableStateOf(listOf<DetalleProductosConsumidosModel>()) }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0E1D)),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1D2B),
                    titleContentColor = Color(0xFF1a1d2b),
                    navigationIconContentColor = Color(0xFF1a1d2b)
                ),
                title = {
                    Text(
                        "Detalle Mesa $numeroMesa",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    val activity = (LocalContext.current as? Activity)
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            DetallesCuentaCard(
                numeroMesa = numeroMesa,
                tipoMesa = tipoMesa,
                clienteNombre = clienteNombre,
                tiempoTranscurrido = tiempoTranscurrido,
                costoTiempo = costoTiempo
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProductosConsumidosSection(
                productos = productos,
                onAnadirClick = {
                    navigateToAddProducto(context, idCuenta, numeroMesa)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            BotonesAccion(
                idCuenta = idCuenta,
                onPagoExitoso = {
                    scope.launch {
                        productos = getDetalleProductosConsumidos(idCuenta)
                    }
                },
                context = context
            )
        }
    }
}

@Composable
fun DetallesCuentaCard(
    numeroMesa: Int,
    tipoMesa: String,
    clienteNombre: String,
    tiempoTranscurrido: String,
    costoTiempo: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1D2B)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mesa $numeroMesa \"$clienteNombre\"",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff7FD238),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tipo de mesa: ${tipoMesa.lowercase()}",
                fontSize = 16.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tiempo: $tiempoTranscurrido",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = "$${String.format("%.2f", costoTiempo)}",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ProductosConsumidosSection(
    productos: List<DetalleProductosConsumidosModel>,
    onAnadirClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1D2B)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Productos consumidos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff7FD238),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Producto",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    "Cantidad",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Total",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Divider(
                color = Color(0xFF2A2D3B),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (productos.isEmpty()) {
                Text(
                    "No hay productos consumidos",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(productos) { producto ->
                            ProductoRow(producto)
                            Divider(
                                color = Color(0xFF2A2D3B),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    // Botones de scroll
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        IconButton(
                            onClick = { /* Scroll hacia arriba */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Scroll arriba",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = { /* Scroll hacia abajo */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Scroll abajo",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onAnadirClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff7FD238)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Añadir",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Añadir",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProductoRow(producto: DetalleProductosConsumidosModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(producto.descripcion, color = Color.White, modifier = Modifier.weight(2f))
        Text("${producto.cantidad}", color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text("$${producto.total}", color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}

@Composable
fun BotonesAccion(
    idCuenta: Int,
    onPagoExitoso: () -> Unit,
    context: Context
) {
    var mostrarDialogoPago by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                val intent = Intent(context, DetalleMesa::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A1D2B)
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Regresar\nmesas",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = { mostrarDialogoPago = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff7FD238)
            ),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Pago",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (mostrarDialogoPago) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoPago = false },
            title = {
                Text(
                    "Confirmar Pago",
                    color = Color(0xff7FD238)
                )
            },
            text = {
                Text(
                    "¿Desea cerrar la cuenta?",
                    color = Color.White
                )
            },
            containerColor = Color(0xFF1A1D2B),
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                val exito = actualizarFechaCierreCuenta(idCuenta)
                                if (exito) {
                                    mostrarDialogoPago = false
                                    onPagoExitoso()
                                    Toast.makeText(context, "Cuenta cerrada exitosamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error al cerrar la cuenta", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text(
                        "Confirmar",
                        color = Color(0xff7FD238)
                    )
                }
            }
            ,
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoPago = false }
                ) {
                    Text(
                        "Cancelar",
                        color = Color.White
                    )
                }
            }
        )
    }
}

fun navigateToAddProducto(context: Context, idCuenta: Int, numeroMesa: Int) {
    val intent = Intent(context, AddProductoToCuenta::class.java).apply {
        putExtra("id_cuenta", idCuenta)
        putExtra("numero_mesa", numeroMesa)
    }
    context.startActivity(intent)
}

fun calcularTiempo(fechaInicio: String): String {
    val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val inicio = formato.parse(fechaInicio)
    val ahora = Date()
    val diferencia = ahora.time - inicio.time

    val horas = (diferencia / (1000 * 60 * 60)).toInt()
    val minutos = ((diferencia / (1000 * 60)) % 60).toInt()
    return "$horas horas y $minutos minutos"
}
fun calcularCosto(tiempo: String): Double {
    val partes = tiempo.split(" ")
    val horas = partes[0].toIntOrNull() ?: 0
    return horas * 40.0
}

suspend fun getDetalleProductosConsumidos(idCuenta: Int): List<DetalleProductosConsumidosModel> {
    return try {
        listOf(
            DetalleProductosConsumidosModel("Producto 1", 2, 50.0),
            DetalleProductosConsumidosModel("Producto 2", 1, 30.0)
        )
    } catch (e: Exception) {
        emptyList()
    }
}

data class DetalleProductosConsumidosModel(
    val descripcion: String,
    val cantidad: Int,
    val total: Double
)
