package com.example.appnotas.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.appnotas.model.Nota

@Database(entities = [Nota::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appaaaadatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
