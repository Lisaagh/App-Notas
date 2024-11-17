package com.example.appnotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.appnotas.ui.theme.AppNotasTheme
import com.example.appnotas.room.AppDatabase
import com.example.appnotas.room.NotaDao
import com.example.appnotas.navigation.NavManager

class MainActivity : ComponentActivity() {

    private lateinit var notaDao: NotaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialización de la base de datos y Dao
        val db = AppDatabase.getDatabase(applicationContext)
        notaDao = db.notaDao()

        setContent {
            AppNotasTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavManager(
                        navController = navController,
                        notaDao = notaDao,
                        context = applicationContext,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
