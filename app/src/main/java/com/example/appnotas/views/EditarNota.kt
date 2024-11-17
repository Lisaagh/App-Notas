package com.example.appnotas.views

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.appnotas.model.Nota
import com.example.appnotas.room.NotaDao
import java.io.File
import java.io.FileOutputStream
import java.util.*
import android.provider.MediaStore
import android.graphics.Bitmap
import androidx.activity.result.launch
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarNota(navController: NavController, nota: Nota, notaDao: NotaDao, context: Context) {
    val scope = rememberCoroutineScope()

    var titulo by remember { mutableStateOf(nota.Titulo) }
    var contenido by remember { mutableStateOf(nota.Contenido) }
    var fondoColor by remember { mutableStateOf(Color(nota.ColorFondo.toInt())) }
    var fondoImagenUriString: String? by remember { mutableStateOf(nota.FondoImagenUri) }
    var fondoImagen: Uri? by remember { mutableStateOf(fondoImagenUriString?.let { Uri.parse(it) }) }
    var textoColor by remember { mutableStateOf(if (fondoColor.luminance() > 0.5) Color.Black else Color.White) }

    val coloresDisponibles = listOf(
        Color.White, Color.Yellow, Color.Cyan, Color.LightGray, Color(0xFFE1BEE7), Color(0xFFBBDEFB)
    )

    // Lanzadores para seleccionar imagen desde los archivos o tomar una foto
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            fondoImagenUriString = it.toString()
            fondoImagen = it
        }
    }

    val cameraPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToTempFile(it, context)
            fondoImagenUriString = uri?.toString()
            fondoImagen = uri
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Editar Nota",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoColor)
        ) {
            fondoImagen?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .background(if (fondoImagen == null) fondoColor else Color.Transparent)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = textoColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text("Contenido") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = textoColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "Última actualización: ${nota.Fecha}",
                    style = MaterialTheme.typography.bodySmall.copy(color = textoColor)
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
                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
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
                                        .width(26.dp)
                                        .height(30.dp)
                                        .background(color, RoundedCornerShape(4.dp))
                                        .clickable {
                                            fondoColor = color
                                            fondoImagen = null
                                            fondoImagenUriString = null
                                            textoColor = if (color.luminance() > 0.5) Color.Black else Color.White
                                        }
                                        .shadow(2.dp, RoundedCornerShape(4.dp))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(48.dp),
                        contentPadding = PaddingValues(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Image, contentDescription = "Galería")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Elegir Imagen")
                    }

                    Button(
                        onClick = { cameraPickerLauncher.launch() },
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(48.dp),
                        contentPadding = PaddingValues(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Cámara")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tomar Foto")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            FloatingActionButton(
                onClick = {
                    scope.launch {
                        val notaActualizada = nota.copy(
                            Titulo = titulo,
                            Contenido = contenido,
                            Fecha = Date().toString(),
                            ColorFondo = fondoColor.toArgb().toLong(),
                            FondoImagenUri = fondoImagenUriString
                        )
                        notaDao.actualizarNota(notaActualizada)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Guardar")
            }
        }
    }
}

fun saveBitmapToTempFile(bitmap: Bitmap, context: Context): Uri? {
    val tempFile = File(context.cacheDir, "temp_image_${UUID.randomUUID()}.jpg")
    try {
        val outStream = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return Uri.fromFile(tempFile)
}
