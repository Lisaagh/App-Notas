package com.example.appnotas.views

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.appnotas.model.Nota
import com.example.appnotas.room.NotaDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.appnotas.ui.theme.AppNotasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notas(navController: NavController, notaDao: NotaDao) {
    val notas by notaDao.obtenerTodasLasNotas().observeAsState(emptyList())
    val scope = rememberCoroutineScope()

    AppNotasTheme {
        val colors = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Mis Notas",
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
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver", tint = colors.onPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.primary,
                        titleContentColor = colors.onPrimary,
                        actionIconContentColor = colors.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("crear_nota") },
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    content = { Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Nota") }
                )
            },
            modifier = Modifier.background(colors.background)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (notas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay notas",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    LazyColumn {
                        items(notas) { nota ->
                            NotaTitleItem(nota = nota, navController = navController, notaDao = notaDao, scope = scope)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotaTitleItem(nota: Nota, navController: NavController, notaDao: NotaDao, scope: CoroutineScope) {
    val fondoColor = Color(nota.ColorFondo.toInt())
    val fondoImagenUri = nota.FondoImagenUri?.let { Uri.parse(it) }

    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable {
                navController.navigate("editar_nota/${nota.id}")
            },
        colors = CardDefaults.cardColors(containerColor = fondoColor),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            fondoImagenUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(fondoColor)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = nota.Titulo,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    scope.launch {
                        notaDao.eliminarNota(nota)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Nota", tint = colors.onSurface)
                }
            }
        }
    }
}
