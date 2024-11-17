package com.example.appnotas.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appnotas.model.Nota

@Dao
interface NotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(nota: Nota)

    @Update
    suspend fun actualizarNota(nota: Nota)

    @Delete
    suspend fun eliminarNota(nota: Nota)

    @Query("SELECT * FROM notas")
    fun obtenerTodasLasNotas(): LiveData<List<Nota>>
}
