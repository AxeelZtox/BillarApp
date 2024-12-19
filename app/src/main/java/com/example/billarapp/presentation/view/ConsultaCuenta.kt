package com.example.billarapp.presentation.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breens.beetablescompose.BeeTablesCompose
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.billarapp.domain.getDetalleProductosConsumidos
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.Manifest
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import com.example.billarapp.ui.theme.*



class DetalleCuenta : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mesa = intent.getStringExtra("mesa") ?: ""
        val cuenta = intent.getStringExtra("cuenta") ?: ""
        val cliente = intent.getStringExtra("cliente") ?: ""
        val tiempo = intent.getStringExtra("tiempo") ?: ""

        setContent {
            DetalleCuentaScreen(this, mesa, cuenta, cliente, tiempo,
                onBackClick = {
                    val activity = (LocalContext as? Activity)
                    activity?.finish()
                })
        }
    }
}

@Composable
fun DetalleCuentaScreen(
    context: Context,
    mesa: String,
    cuenta: String,
    cliente: String,
    tiempo: String,
    onBackClick: () -> Unit
) {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    var hasRequestedPermission by remember { mutableStateOf(false) }

    //Solicitar permisos
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasRequestedPermission) {
            hasRequestedPermission = true
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    var mesaText by remember { mutableStateOf(mesa) }
    var cuentaText by remember { mutableStateOf(cuenta) }
    var nombreCliente by remember { mutableStateOf(cliente) }
    var tiempoText by remember { mutableStateOf(tiempo) }

    // Obtener los minutos totales del tiempo
    val minutosTotales = calcularMinutosDesdeTiempo(tiempo)
    val totalPorTiempo = minutosTotales * 0.66

    // Obtener productos consumidos
    val productosConsumidos = getDetalleProductosConsumidos(cuentaText.toInt())

    // Calcular el total de los productos
    val totalProductos = productosConsumidos.sumOf { it.total } // Sumar los totales de la tabla

    // Total final
    val totalPagar = "%.2f".format(totalPorTiempo + totalProductos)

    val tableHeaders = listOf("Detalle", "Cantidad", "Total")

    Scaffold(
        topBar = {
            //Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VerdeSecundario)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = TextoBlanco,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {onBackClick()}
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Regresar",
                    color = TextoBlanco,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,

                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FondoOscuro)
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Detalle de Cuenta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdePrimario,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                //Información
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mesa:", fontWeight = FontWeight.Bold, color = VerdePrimario)
                        TextField(
                            value = mesaText,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFE7F0))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Cliente:", fontWeight = FontWeight.Bold, color = VerdePrimario)
                        TextField(
                            value = nombreCliente,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFE7F0))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Cuenta:", fontWeight = FontWeight.Bold, color = VerdePrimario)
                        TextField(
                            value = cuentaText,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFE7F0))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Información Adicional
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tiempo:", fontWeight = FontWeight.Bold, color = VerdePrimario)
                        TextField(
                            value = tiempoText,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFE7F0))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total a Pagar:", fontWeight = FontWeight.Bold, color = VerdePrimario)
                        TextField(
                            value = "$$totalPagar",
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFE7F0))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Productos Consumidos
                Text(
                    "Productos Consumidos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = VerdePrimario,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))

                //Encabezado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VerdeSecundario)
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        tableHeaders.forEach { header ->
                            Text(
                                text = header,
                                color = TextoBlanco,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                //Contenido de tabla
                productosConsumidos.forEach { producto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Text(producto.descripcion, color = TextoBlanco, modifier = Modifier.weight(1f))
                        Text("${producto.cantidad}", color = TextoBlanco, modifier = Modifier.weight(1f))
                        Text("$${producto.total}", color = TextoBlanco, modifier = Modifier.weight(1f))
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = {
                            val productosConvertidos = productosConsumidos.map {
                                Producto(
                                    descripcion = it.descripcion,
                                    cantidad = it.cantidad,
                                    total = it.total
                                )
                            }
                            generateDetalleCuentaPdf(
                                context = context,
                                cuenta = cuentaText,
                                mesa = mesaText,
                                cliente = nombreCliente,
                                fechaInicio = "2024-10-22 11:15:31",
                                fechaFin = "2024-10-22 11:22:20",
                                tiempo = tiempoText,
                                totalTiempo = "$totalPagar",
                                productos = productosConvertidos
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeSecundario)
                    ) {
                        Text("Generar PDF", color = TextoBlanco)
                    }
                }
            }
        }
    )
}

fun calcularMinutosDesdeTiempo(tiempo: String): Int {
    val horasRegex = """(\d+)\s*horas""".toRegex()
    val minutosRegex = """(\d+)\s*minutos""".toRegex()

    val horas = horasRegex.find(tiempo)?.groupValues?.get(1)?.toIntOrNull() ?: 0
    val minutos = minutosRegex.find(tiempo)?.groupValues?.get(1)?.toIntOrNull() ?: 0

    return horas * 60 + minutos
}

fun generateDetalleCuentaPdf(
    context: Context,
    cuenta: String,
    mesa: String,
    cliente: String,
    fechaInicio: String,
    fechaFin: String,
    tiempo: String,
    totalTiempo: String,
    productos: List<Producto>
) {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = document.startPage(pageInfo)
    val canvas = page.canvas
    val paint = android.graphics.Paint()
    paint.textSize = 12f

    var startX = 50f
    var startY = 50f
    val lineSpacing = 20f

    // Encabezado
    paint.isFakeBoldText = true

    canvas.drawText(
        "Cuenta: $cuenta     Mesa: $mesa     Fecha inicio: $fechaInicio     Fecha fin: $fechaFin",
        startX,
        startY,
        paint
    )
    startY += lineSpacing//Salto de línea
    canvas.drawText("Cliente: $cliente", startX, startY, paint)
    startY += lineSpacing * 2

    //Tabla
    val headers = listOf("Detalle producto", "Cantidad", "Total")
    val columnWidths = listOf(250f, 100f, 100f)

    var currentX = startX
    headers.forEachIndexed { index, header ->
        canvas.drawText(header, currentX, startY, paint)
        currentX += columnWidths[index]
    }
    startY += lineSpacing
    canvas.drawLine(startX, startY - 15f, startX + columnWidths.sum(), startY - 15f, paint)

    //Contenido de la tabla
    paint.isFakeBoldText = false
    productos.forEach { producto ->
        currentX = startX
        canvas.drawText(producto.descripcion, currentX, startY, paint)
        currentX += columnWidths[0]
        canvas.drawText("${producto.cantidad}", currentX, startY, paint)
        currentX += columnWidths[1]
        canvas.drawText("$${"%.2f".format(producto.total)}", currentX, startY, paint)
        startY += lineSpacing
    }

    //Tiempo y total
    canvas.drawText("Tiempo: $tiempo", startX, startY, paint)
    canvas.drawText("Total a pagar: $totalTiempo", startX, startY + lineSpacing, paint)
    document.finishPage(page)

    //Guardar en Descargas
    val downloadsDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloadsDir, "detalleCuenta.pdf")

    try {
        val outputStream = FileOutputStream(file)
        document.writeTo(outputStream)
        document.close()
        outputStream.close()
        Toast.makeText(
            context,
            "PDF guardado en Descargas",
            Toast.LENGTH_LONG
        ).show()

        //Opciones de abrir el PDF
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_LONG).show()
    }
}

data class Producto(
    val descripcion: String,
    val cantidad: Int,
    val total: Double
)