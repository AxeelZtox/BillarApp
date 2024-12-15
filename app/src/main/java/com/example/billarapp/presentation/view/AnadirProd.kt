package com.example.billarapp.presentation.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.breens.beetablescompose.BeeTablesCompose
import com.example.billarapp.R
import com.example.billarapp.domain.InsertProducto
import com.example.billarapp.domain.ProveedoresModel
import com.example.billarapp.domain.getProductosFromDataBase
import com.example.billarapp.domain.getProvedoresFromDataBase
import com.example.billarapp.presentation.controller.MiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AnadirProd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent{
            ShowContent()
        }
    }
}



@Preview(showSystemUi = true)
@Preview(showBackground = true)
@Composable
private fun ShowContent(){
    ProductosScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductosScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = (Color(0xFFB7BABC)),
                    titleContentColor = Color(0xFF0B0E1D),
                    navigationIconContentColor = Color(0xFF0B0E1D)
                ),
                title = {
                    Text(
                        "Añadir Productos",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    val activity = (LocalContext.current as? Activity)
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
                actions = {
                    //Ese boton debe llamar al menu principal
                    val activity = (LocalContext.current as? Activity)
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Filled.Home, contentDescription = "Localized description")
                    }
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCD6D))
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                TextFieldsAnadirProd()
            }
        }
    )

}



@Composable
private fun TextFieldsAnadirProd() {
    val producto = remember { mutableStateOf(TextFieldValue("")) }
    val precio = remember { mutableStateOf(TextFieldValue("")) }
    var proveedor by remember { mutableStateOf(0) }
    val stock = remember{ mutableStateOf(TextFieldValue("")) }

    Row (verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(20.dp)){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center

    ) {
        /*Seccion nombre del producto*/
        val viewModelProducto: MiViewModel = MiViewModel()

        Text(text = "Nombre del producto")
        Spacer(modifier = Modifier.height(10.dp))
        NombreProductoTextField(producto)
        Spacer(modifier = Modifier.height(18.dp))

        /*Seccion precio del producto*/

        Text(text = "Precio")
        Spacer(modifier = Modifier.height(10.dp))
        PrecioTextField(precio)

        /*Seccion lista de proveedores*/
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = "Proveedores")
        DropdownMenuProveedores(proveedorDrop = proveedor,
            onProveedorChanged = {nuevoProveedor ->
                proveedor =nuevoProveedor
            })


        /*Seccion stock*/
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = "Inventario")
        Spacer(modifier = Modifier.height(10.dp))
        StockTextField(stock)


        Spacer(modifier = Modifier.height(30.dp))
        ButtonSubirProducto(producto,precio,proveedor,stock)


    }
    }
}


/*Dropdown de proveedores*/
@Composable
fun DropdownMenuProveedores(
    proveedorDrop: Int,
    onProveedorChanged: (Int) -> Unit
) {
    val proveedores = getProvedoresFromDataBase()
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableStateOf(0) }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { isDropDownExpanded.value = true }
            .background(Color(0xFFFFFFFF))
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (proveedores.isNotEmpty()) {
            Text(text = proveedores[itemPosition.value].nombre)
        } else {
            Text(text = "No hay proveedores")
        }
        Icon(Icons.Rounded.ArrowDropDown, contentDescription = "Mostrar proveedores")
        MenuProv(isDropDownExpanded, proveedores, itemPosition)
    }

    // Notificar el cambio de proveedor seleccionado
    if (proveedores.isNotEmpty()) {
        onProveedorChanged(proveedores[itemPosition.value].id_proveedor)
    }
}


/*Menu que saldra al dar click en proveedores*/
@Composable
fun MenuProv(isDropDownExpanded: MutableState<Boolean>,proveedores:MutableList<ProveedoresModel>,itemPosition: MutableState<Int>){
    DropdownMenu(
        expanded = isDropDownExpanded.value,
        onDismissRequest = {
            isDropDownExpanded.value = false}
    ) {
        proveedores.forEachIndexed{
                index, nombre ->
            DropdownMenuItem(text = {
                Text(text = nombre.nombre)
            },
                onClick = {
                    isDropDownExpanded.value = false
                    itemPosition.value = index
                }
            )
        }
    }
}


/*TextField encargada de almacenar el producto*/
@Composable
fun NombreProductoTextField(producto:MutableState<TextFieldValue>) {
    OutlinedTextField(
        value = producto.value,
        onValueChange = { input->
            producto.value = input
        },
        leadingIcon = {Icon(Icons.Filled.Edit,"Producto")},
        label = { Text(text = "Producto") },
        placeholder = { Text(text = "Nombre del producto") },

    )
}


/*Texfield del inventario*/
@Composable
fun StockTextField(stock: MutableState<TextFieldValue>) {
    val pattern = remember { Regex("^\\d*$") } // Solo permite números enteros

    OutlinedTextField(
        value = stock.value,
        onValueChange = { input ->
            // Validar si el input es válido o está vacío
            if (pattern.matches(input.text) || input.text.isEmpty()) {
                stock.value = input
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(text = "Cantidad en Inventario") },
        placeholder = { Text(text = "Ingresa cantidad") },
        modifier = Modifier.background(Color(0xFFFFFFFF))
    )
}


/*Textfield del precio*/
@Composable
fun PrecioTextField(precio: MutableState<TextFieldValue>) {
    val pattern = remember { Regex("^\\d*(\\.\\d{0,2})?$") } // Permite hasta 2 decimales

    OutlinedTextField(
        value = precio.value,
        onValueChange = { input ->
            // Validar que cumpla el patrón o que esté vacío
            if (pattern.matches(input.text) || input.text.isEmpty()) {
                precio.value = input
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(text = "Precio") },
        placeholder = { Text(text = "Precio") },
        leadingIcon = { Icon(Icons.Filled.Check, contentDescription = "Precio del producto") },
        modifier = Modifier.background(Color(0xFFFFFFFF))
    )
}


/*Boton que sube el producto*/
@Composable
fun ButtonSubirProducto(producto: MutableState<TextFieldValue>, precio: MutableState<TextFieldValue>, proveedor: Int, stock: MutableState<TextFieldValue>) {
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoFaltanCampos by remember { mutableStateOf(false) }
    var mensajeDialogoExito by remember { mutableStateOf("") }
    val context = LocalContext.current


    ElevatedButton(onClick = {
        // Valida los datos antes de enviarlos
        if (producto.value.text.isNotBlank() && precio.value.text.isNotBlank() && proveedor!=0 && stock.value.text.isNotBlank()) {
            mostrarDialogoConfirmacion = true

        } else {
            mostrarDialogoFaltanCampos= true
        }
    }) {
        Text("Añadir Producto")
    }

    if(mostrarDialogoConfirmacion){
        MostrarDialogoConfirmacion(
            producto = producto.value.text,
            onConfirmar = {
                mostrarDialogoConfirmacion = false
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        InsertProducto(
                            proveedor = proveedor,
                            precio = precio,
                            producto = producto,
                            stock = stock
                        )
                        mensajeDialogoExito = "Producto añadido con exito"
                    }catch (e:Exception){
                        mensajeDialogoExito = "Error al subir el producto: ${e.message}"
                    }
                    mostrarDialogoExito = true
                }
            },
            onCancelar = {mostrarDialogoConfirmacion=false}
        )
    }
    if (mostrarDialogoExito){
        MostarDialogoExito(
            mensaje = mensajeDialogoExito,
            onDismiss = {mostrarDialogoExito = false},
            context = context
        )
    }

    if(mostrarDialogoFaltanCampos){
        MostrarDialogoFaltanCampos { mostrarDialogoFaltanCampos = false }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarDialogoConfirmacion(
    producto: String,
    onConfirmar: ()->Unit,
    onCancelar:()->Unit
){

    AlertDialog(
        onDismissRequest = {onCancelar()},
        title = { Text("Confirmar") },
        text = { Text("¿Deseas agregar el producto $producto a la lista?") },
        confirmButton = {
            TextButton(onClick = {onConfirmar()}) {
                Text("Si")
            }
        },
        dismissButton = {
            TextButton(onClick = {onCancelar()}) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun MostrarDialogoFaltanCampos(onDismiss: ()->Unit){
    AlertDialog(
        onDismissRequest = {onDismiss()},
        title = { Text("Error") },
        text = {Text("Por favor, completa todos los campos antes de continuar")},
        confirmButton = {
            TextButton(onClick = {onDismiss()}) {
                Text("Aceptar")
            }
        }
    )
}


@Composable
fun MostarDialogoExito(mensaje: String, onDismiss: () -> Unit, context: Context){
    AlertDialog(
        onDismissRequest = {onDismiss()},
        title = {Text("Resultado")},
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                //Nav to Productos
                val intent = Intent (context,Productos::class.java)
                context.startActivity(intent)
            }) {
                Text("Aceptar")
            }
        }
    )
}







