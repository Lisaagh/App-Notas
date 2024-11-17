package com.example.appnotas.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appnotas.model.Nota
import com.example.appnotas.room.NotaDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearNota(navController: NavController, notaDao: NotaDao) {
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var fondoColor by remember { mutableStateOf(Color.White) }
    val fecha = remember { mutableStateOf(getCurrentDate()) }

    val coroutineScope = rememberCoroutineScope()

    val coloresDisponibles = listOf(
        Color.White, Color.Yellow, Color.Cyan, Color.LightGray, Color(0xFFE1BEE7), Color(0xFFBBDEFB)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Nueva Nota",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val nuevaNota = Nota(
                        Titulo = titulo,
                        Contenido = contenido,
                        Fecha = fecha.value,
                        ColorFondo = fondoColor.toArgb().toLong()
                    )
                    coroutineScope.launch {
                        notaDao.insertarNota(nuevaNota)
                    }
                    navController.popBackStack()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                content = { Icon(imageVector = Icons.Default.Save, contentDescription = "Guardar Nota") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoColor)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(if (fondoColor == Color.Transparent) Color.Transparent else fondoColor)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text("Contenido") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium)
                            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            coloresDisponibles.forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp)
                                        .background(color, shape = MaterialTheme.shapes.small)
                                        .clickable {
                                            fondoColor = color
                                        }
                                        .shadow(2.dp, MaterialTheme.shapes.small)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date())
}
