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

    // El método onCreate se llama cuando se crea la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el diseño de la interfaz de usuario definido en activity_main.xml.
        setContentView(R.layout.activity_main)

        // Inicializa las variables con los elementos de la interfaz de usuario.
        etNombre = findViewById(R.id.etNombreDisco)
        etAnio = findViewById(R.id.etAnioPublicacion)
        btnAgregar = findViewById(R.id.btnAdd)
        btnVerTodos = findViewById(R.id.btnViewAll)
        recyclerView = findViewById(R.id.rvDiscos)
        // Inicializa el controlador de la base de datos.
        dbHandler = DatabaseHelper(this)

        // Configura los eventos de clic para los botones.
        btnAgregar.setOnClickListener { addDisco() }
        btnVerTodos.setOnClickListener { viewDiscos() }

        // Muestra la lista de discos al iniciar la actividad.
        viewDiscos()
    }

    // Método para agregar un nuevo disco a la base de datos.
    private fun addDisco() {
        // Obtiene el texto de los EditText y lo convierte en String.
        val nombre = etNombre.text.toString()
        val anio = etAnio.text.toString()
        // Verifica que los campos no estén vacíos.
        if (nombre.isNotEmpty() && anio.isNotEmpty()) {
            // Crea un objeto Disco y lo añade a la base de datos.
            val disco = Disco(nombre = nombre, anio = anio.toInt())
            val status = dbHandler.addDisco(disco)
            // Verifica si la inserción fue exitosa.
            if (status > -1) {
                Toast.makeText(applicationContext, "Disco agregado", Toast.LENGTH_LONG).show()
                // Limpia los campos de texto y actualiza la vista de discos.
                clearEditTexts()
                viewDiscos()
            }
        } else {
            // Muestra un mensaje si los campos están vacíos.
            Toast.makeText(applicationContext, "Nombre y Año son requeridos", Toast.LENGTH_LONG).show()
        }
    }

    // Método para mostrar todos los discos en el RecyclerView.
    private fun viewDiscos() {
        // Obtiene la lista de discos de la base de datos.
        val discosList = dbHandler.getAllDiscos()
        // Crea un adaptador para el RecyclerView y lo configura.
        val adapter = DiscosAdapter(discosList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    // Método para limpiar los campos de texto.
    private fun clearEditTexts() {
        etNombre.text.clear()
        etAnio.text.clear()
    }
}
