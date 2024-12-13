package com.example.t8_ej01_persistenciadatossqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Clase DatabaseHelper que extiende SQLiteOpenHelper para manejar la base de datos de la aplicación.
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Bloque companion object para definir constantes que serán usadas en toda la clase.
    // Son como los valores estáticos en Java
    // Nombre de la base de datos.
    // Versión de la base de datos, útil para manejar actualizaciones esquemáticas.
    // Nombre de la tabla donde se almacenarán los discos.
    // Nombres de las columnas de la tabla.

    companion object {
        private const val DATABASE_NAME = "DiscosDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_DISCOS = "discos"
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_ANIO = "anio"
    }

    // Función llamada cuando la base de datos se crea por primera vez.
    // Define la sentencia SQL para crear la tabla de discos.
    // Ejecuta la sentencia SQL para crear la tabla.

    override fun onCreate(db: SQLiteDatabase) {
        val createDiscosTable = ("CREATE TABLE " + TABLE_DISCOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOMBRE + " TEXT,"
                + KEY_ANIO + " INTEGER" + ")")
        db.execSQL(createDiscosTable)
    }

    // Función llamada cuando se necesita actualizar la base de datos, por ejemplo, cuando se incrementa DATABASE_VERSION.
    // Elimina la tabla existente y crea una nueva.

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DISCOS")
        onCreate(db)
    }

    // Función para obtener todos los discos de la base de datos.
    // Lista para almacenar y retornar los discos.
    // Consulta SQL para seleccionar todos los discos.
    // Maneja la excepción en caso de error al ejecutar la consulta.
    fun getAllDiscos(): ArrayList<Disco> {
        val discosList = ArrayList<Disco>()
        val selectQuery = "SELECT  * FROM $TABLE_DISCOS"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        // Variables para almacenar los valores de las columnas.
        var id: Int
        var nombre: String
        var anio: Int

        // Itera a través del cursor para leer los datos de la base de datos.
        // Obtiene los índices de las columnas.
        // Verifica que los índices sean válidos.
        // Lee los valores y los añade a la lista de discos.

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val anioIndex = cursor.getColumnIndex(KEY_ANIO)

                if (idIndex != -1 && nombreIndex != -1 && anioIndex != -1) {
                    id = cursor.getInt(idIndex)
                    nombre = cursor.getString(nombreIndex)
                    anio = cursor.getInt(anioIndex)

                    val disco = Disco(id = id, nombre = nombre, anio = anio)
                    discosList.add(disco)
                }
            } while (cursor.moveToNext())
        }

        // Cierra el cursor para liberar recursos.
        cursor.close()
        return discosList
    }

    // Función para actualizar un disco en la base de datos.
    // Prepara los valores a actualizar.
    // Actualiza la fila correspondiente y retorna el número de filas afectadas.
    fun updateDisco(disco: Disco): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NOMBRE, disco.nombre)
        contentValues.put(KEY_ANIO, disco.anio)

        return db.update(TABLE_DISCOS, contentValues, "$KEY_ID = ?", arrayOf(disco.id.toString()))
    }

    // Función para eliminar un disco de la base de datos.
    // Elimina la fila correspondiente y retorna el número de filas afectadas.
    fun deleteDisco(disco: Disco): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_DISCOS, "$KEY_ID = ?", arrayOf(disco.id.toString()))
        db.close()
        return success
    }

    // Función para añadir un nuevo disco a la base de datos.
    // Prepara los valores a insertar.
    // Inserta el nuevo disco y retorna el ID del nuevo disco o -1 en caso de error.
    // Maneja la excepción en caso de error al insertar.

    fun addDisco(disco: Disco): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_NOMBRE, disco.nombre)
            contentValues.put(KEY_ANIO, disco.anio)

            val success = db.insert(TABLE_DISCOS, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            Log.e("Error", "Error al agregar disco", e)
            return -1
        }
    }
}