package com.example.billarapp.presentation.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    context: Context, // Pasamos el contexto para usar Intents
    onPoolClick: () -> Unit,
    onCarambolaClick: () -> Unit,
    onMesaClick: (Int) -> Unit,
    onCuentasClick: () -> Unit,
    onProveedoresClick: () -> Unit
) {
    val mesas = listOf("Mesa 1", "Mesa 2", "Mesa 3", "Mesa N")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mesas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1D2B),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF1A1D2B),
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BottomNavigationButton(
                        text = "Proveedores",
                        icon = Icons.Default.Group,
                        onClick = onProveedoresClick
                    )
                    BottomNavigationButton(
                        text = "POOL",
                        icon = Icons.Default.AddCircle,
                        onClick = onPoolClick
                    )
                    BottomNavigationButton(
                        text = "CARAMBOLA",
                        icon = Icons.Default.AddCircle,
                        onClick = onCarambolaClick
                    )
                    BottomNavigationButton(
                        text = "Cuentas",
                        icon = Icons.Default.Home,
                        onClick = onCuentasClick
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0E1D))
                    .padding(innerPadding)
            ) {
                mesas.forEachIndexed { index, mesa ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onMesaClick(index + 1) // Usa el callback para manejar el clic en cada mesa
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1D2B)
                        )
                    ) {
                        Text(
                            text = mesa,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun BottomNavigationButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = text, tint = Color.White, modifier = Modifier.size(24.dp))
        Text(text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
