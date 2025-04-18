package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.util.copy
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.UUID

class NuevaTareaCasaActivity : AppCompatActivity() {

    private lateinit var usuario: Usuario
    private lateinit var hogar: Hogar
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_tarea_casa)

        db = FirebaseFirestore.getInstance()

        usuario = intent.getSerializableExtra("usuario") as Usuario
        hogar = intent.getSerializableExtra("hogar") as Hogar

        val nameInput = findViewById<EditText>(R.id.activityNameInput)
        val descInput = findViewById<EditText>(R.id.activityDescInput)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)
        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener { finish() }

        btnAgregar.isEnabled = true
        btnAgregar.setOnClickListener {
            val nombre = nameInput.text.toString().trim()
            val descripcion = descInput.text.toString().trim()

            if (nombre.isEmpty() || descripcion.isEmpty()) {
                errorTextView.text = "Todos los campos son obligatorios"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val nuevaTarea = Tarea(
                id = UUID.randomUUID().toString(),
                nombreTarea = nombre,
                descripcion = descripcion
            )

            val tareasActualizadas = hogar?.tareas?.toMutableList() ?: mutableListOf()
            tareasActualizadas.add(nuevaTarea)

            val db = Firebase.firestore
            val hogarRef = db.collection("hogares").document(hogar!!.id)

            hogarRef.update("tareas", tareasActualizadas)
                .addOnSuccessListener {
                    val intent = Intent(this, MenuActivity::class.java).apply {
                        putExtra("hogar", hogar!!.copy(tareas = tareasActualizadas)) // esta madre es pa pasar el objeto actualizado
                        putExtra("usuario", usuario)
                    }
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    errorTextView.text = "Error al guardar tarea: ${it.message}"
                    errorTextView.visibility = View.VISIBLE
                }
        }
    }
}