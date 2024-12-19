package com.example.billarapp.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.billarapp.R
import com.example.billarapp.domain.InsertCuenta
import com.example.billarapp.ui.theme.SystemBarsColorChanger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrearCuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Variable para especificar el billar el cual se operara
        //val id_billar: Int =intent.getParcelableExtra("id_billar")!!
        //val numero_mesa: Int =intent.getParcelableExtra("id_billar")!!



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
                    ShowContent(1,1)
                }
            }
        }

    }

}

@Composable
private fun ShowContent(id_billar:Int,numero_mesa:Int){
    CrearCuentaScreen(id_billar,numero_mesa,
        onBackClick = {
            val activity = (LocalContext as? Activity)
            activity?.finish()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCuentaScreen(id_billar:Int,numero_mesa:Int, onBackClick: () -> Unit){
    /*Variables y funciones*/
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var cliente by remember { mutableStateOf("") }
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }


    if(mostrarDialogoConfirmacion){
        AlertDialog(
            onDismissRequest = {mostrarDialogoConfirmacion=false},
            title = {Text("Agregar cliente")},
            text = { Text("Crear nueva cuenta para cliente ${cliente}") },
            confirmButton = {
                TextButton(onClick = {
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            if(InsertCuenta(id_billar,cliente,numero_mesa)){
                                val intent = Intent(context, Productos::class.java)/*.apply {
                                putExtra("id_billar",id_billar)
                                putExtra("numero_mesa",numero_mesa)
                            }*/
                                context.startActivity(intent)
                            }
                                                    /*Cambiar a Cuenta de mesa*/


                        }
                    }catch (e:Exception){
                        Log.e("dbg","Error: ${e.message}")
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = {mostrarDialogoConfirmacion = false}) {
                    Text("Cancelar")
                }
            }
        )
    }





    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1D2B),
                    titleContentColor = Color(0xFFffffff),
                    navigationIconContentColor = Color(0xFFFFFFFF)
                ),
                title = {
                    Text(
                        "Añadir Mesas",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },


                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
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
                    //No hace nada
                },
            )
        },
        content = {innerPadding->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0E1D))
                    .padding(innerPadding)
                    .padding(18.dp)
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    "CREAR CUENTA",
                    color = Color(0xff7FD238),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(Modifier.height(15.dp))
                OutlinedTextField(
                    value = cliente,
                            onValueChange = { input ->
                        // Validar si el input es válido o está vacío
                                cliente=input
                    },
                    label = { Text("Nombre del cliente", color = Color.White) },
                    placeholder = { Text(text = "Ingrese nombre") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xff99df5b),
                        unfocusedBorderColor = Color(0xFFB2B6C1),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Mesas",
                            tint = Color(0xFF99DF5B)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        mostrarDialogoConfirmacion= true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff7FD238)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Agregar",
                        tint = Color(0xFF0B0E1D)
                    )
                }
            }
        }
    )

}