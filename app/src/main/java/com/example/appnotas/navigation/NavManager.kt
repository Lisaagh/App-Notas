package com.example.appnotas.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appnotas.views.Inicio
import com.example.appnotas.views.Notas
import com.example.appnotas.views.CrearNota
import com.example.appnotas.views.EditarNota
import com.example.appnotas.room.NotaDao
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun NavManager(
    navController: NavHostController,
    notaDao: NotaDao,
    context: Context,
    modifier: Modifier = Modifier
) {
    val notas by notaDao.obtenerTodasLasNotas().observeAsState(emptyList())

    NavHost(
        navController = navController,
        startDestination = NavDestinations.Inicio,
        modifier = modifier
    ) {
        composable(NavDestinations.Inicio) {
            Inicio(navController)
        }
        composable(NavDestinations.Notas) {
            Notas(navController = navController, notaDao = notaDao)
        }
        composable(NavDestinations.CrearNota) {
            CrearNota(navController = navController, notaDao = notaDao)
        }
        composable("${NavDestinations.EditarNota}/{idNota}") { backStackEntry ->
            val idNota = backStackEntry.arguments?.getString("idNota")?.toIntOrNull()
            val nota = idNota?.let { id -> notas.find { it.id == id } }

            if (nota != null) {
                EditarNota(navController = navController, nota = nota, notaDao = notaDao, context = context)
            } else {
            }
        }
    }
}