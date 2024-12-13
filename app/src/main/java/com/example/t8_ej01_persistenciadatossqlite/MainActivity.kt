package com.example.t8_ej01_persistenciadatossqlite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Define la clase MainActivity que hereda de AppCompatActivity, una clase base para actividades que usan las características de la ActionBar de Android.
class MainActivity : AppCompatActivity() {

    // Declara variables para los elementos de la interfaz de usuario y la base de datos.
    // lateinit indica que estas variables se inicializarán más tarde.
    private lateinit var etNombre: EditText
    private lateinit var etAnio: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnVerTodos: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHandler: DatabaseHelper
    private lateinit var btnBorrar: Button
    private lateinit var btnActualizar: Button
    private lateinit var etID: EditText

    // El métoodo onCreate se llama cuando se crea la actividad.
    // Establece el diseño de la interfaz de usuario definido en activity_main.xml.
    // Inicializa las variables con los elementos de la interfaz de usuario.
    // Inicializa el controlador de la base de datos.
    // Configura los eventos de clic para los botones.
    // Muestra la lista de discos al iniciar la actividad.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNombre = findViewById(R.id.etNombreDisco)
        etAnio = findViewById(R.id.etAnioPublicacion)
        btnAgregar = findViewById(R.id.btnAdd)
        btnVerTodos = findViewById(R.id.btnViewAll)
        btnBorrar = findViewById(R.id.Borrar)
        btnActualizar = findViewById(R.id.Actualizar)
        recyclerView = findViewById(R.id.rvDiscos)
        dbHandler = DatabaseHelper(this)
        etID = findViewById(R.id.etID)

        btnAgregar.setOnClickListener { addDisco() }
        btnVerTodos.setOnClickListener { viewDiscos() }
        btnBorrar.setOnClickListener { borrarDisco() }
        btnActualizar.setOnClickListener { actualizarDisco() }

        viewDiscos()
    }

    private fun actualizarDisco() {
        val nombre = etNombre.text.toString()
        val anio = etAnio.text.toString()
        val id = etID.text.toString()
        if (nombre.isNotEmpty() && anio.isNotEmpty() && id.isNotEmpty()) {
            val disco = Disco(nombre = nombre, anio = anio.toInt(), id = id.toInt())
            val status = dbHandler.updateDisco(disco)
            if (status > -1) {
                Toast.makeText(applicationContext, "Disco actualizado", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewDiscos()
            }
        } else {
            Toast.makeText(applicationContext, "Nombre, Año e id son requeridos", Toast.LENGTH_LONG).show()
        }
    }

    private fun borrarDisco() {
       val id = etID.text.toString()
        if (id.isNotEmpty()) {
            val disco = Disco(id = id.toInt())
            val status = dbHandler.deleteDisco(disco)
            if (status > -1) {
                Toast.makeText(applicationContext, "Disco eliminado", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewDiscos()
            }else {
                Toast.makeText(applicationContext, "id es requerido", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Métoodo para agregar un nuevo disco a la base de datos.
    // Obtiene el texto de los EditText y lo convierte en String.
    // Verifica que los campos no estén vacíos.
    // Crea un objeto Disco y lo añade a la base de datos.
    // Verifica si la inserción fue exitosa.
    // Limpia los campos de texto y actualiza la vista de discos.
    // Muestra un mensaje si los campos están vacíos.
    private fun addDisco() {
        val nombre = etNombre.text.toString()
        val anio = etAnio.text.toString()
        if (nombre.isNotEmpty() && anio.isNotEmpty()) {
            val disco = Disco(nombre = nombre, anio = anio.toInt())
            val status = dbHandler.addDisco(disco)
            if (status > -1) {
                Toast.makeText(applicationContext, "Disco agregado", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewDiscos()
            }
        } else {
            Toast.makeText(applicationContext, "Nombre y Año son requeridos", Toast.LENGTH_LONG).show()
        }
    }

    // Métodoo para mostrar todos los discos en el RecyclerView.
    // Obtiene la lista de discos de la base de datos.
    // Crea un adaptador para el RecyclerView y lo configura.

    private fun viewDiscos() {
        val discosList = dbHandler.getAllDiscos()
        val adapter = DiscosAdapter(discosList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    // Métodoo para limpiar los campos de texto.
    private fun clearEditTexts() {
        etNombre.text.clear()
        etAnio.text.clear()
    }
}
