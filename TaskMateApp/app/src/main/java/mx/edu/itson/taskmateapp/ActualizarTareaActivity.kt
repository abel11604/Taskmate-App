 package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore

 class ActualizarTareaActivity : AppCompatActivity() {

     private lateinit var nombreEditText: EditText
     private lateinit var descripcionEditText: EditText
     private lateinit var actualizarButton: Button
     private lateinit var errorTextView: TextView
     private lateinit var backButton: ImageView

     private lateinit var tarea: Tarea
     private lateinit var hogar: Hogar
     private val db = FirebaseFirestore.getInstance()

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()
         setContentView(R.layout.activity_actualizar_tarea)

         nombreEditText = findViewById(R.id.activityNameInput)
         descripcionEditText = findViewById(R.id.activityDescInput)
         actualizarButton = findViewById(R.id.btnActualizar)
         errorTextView = findViewById(R.id.errorTextView)
         backButton = findViewById(R.id.backButton)

         tarea = intent.getSerializableExtra("tarea") as Tarea
         hogar = intent.getSerializableExtra("hogar") as Hogar

         // Mostrar datos actuales
         nombreEditText.setText(tarea.nombreTarea)
         descripcionEditText.setText(tarea.descripcion)

         // Detectar cambios
         val textWatcher = object : TextWatcher {
             override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
             override fun afterTextChanged(s: Editable?) {}
             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 actualizarButton.isEnabled = camposHanCambiado()
                 errorTextView.visibility = TextView.GONE
             }
         }

         nombreEditText.addTextChangedListener(textWatcher)
         descripcionEditText.addTextChangedListener(textWatcher)

         backButton.setOnClickListener { finish() }

         actualizarButton.setOnClickListener {
             val nuevoNombre = nombreEditText.text.toString().trim()
             val nuevaDescripcion = descripcionEditText.text.toString().trim()

             if (nuevoNombre.isEmpty() || nuevaDescripcion.isEmpty()) {
                 errorTextView.text = "Todos los campos son obligatorios"
                 errorTextView.visibility = TextView.VISIBLE
                 return@setOnClickListener
             }

             val nuevasTareas = hogar.tareas.map {
                 if (it.id == tarea.id) {
                     Tarea(it.id, nuevoNombre, nuevaDescripcion)
                 } else it
             }

             db.collection("hogares")
                 .document(hogar.id)
                 .update("tareas", nuevasTareas.map {
                     mapOf(
                         "id" to it.id,
                         "nombreTarea" to it.nombreTarea,
                         "descripcion" to it.descripcion
                     )
                 })
                 .addOnSuccessListener {
                     val intent = Intent(this, MenuActivity::class.java).apply {
                         putExtra("hogar", hogar.copy(tareas = nuevasTareas))
                         putExtra("usuario", intent.getSerializableExtra("usuario"))
                     }
                     startActivity(intent)
                     finish()
                 }
                 .addOnFailureListener {
                     errorTextView.text = "Error al actualizar: ${it.message}"
                     errorTextView.visibility = TextView.VISIBLE
                 }
         }
     }

     private fun camposHanCambiado(): Boolean {
         return nombreEditText.text.toString().trim() != tarea.nombreTarea ||
                 descripcionEditText.text.toString().trim() != tarea.descripcion
     }
 }
