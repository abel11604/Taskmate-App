package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity() {

    private lateinit var usuario: Usuario
    private lateinit var hogar: Hogar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_usuario)

        // Obtener usuario y hogar del Intent
        usuario = intent.getSerializableExtra("usuario") as Usuario
        hogar = intent.getSerializableExtra("hogar") as Hogar

        // Inicializar vistas
        val nameEditText: TextInputEditText = findViewById(R.id.nameEditText)
        val errorTextView: TextView = findViewById(R.id.errorTextView)  // TextView para errores
        val updateButton: Button = findViewById(R.id.updateButton)

        // Mostrar el nombre actual del usuario en el campo de texto
        nameEditText.setText(usuario.username)

        // Manejar la acción del botón de actualización
        updateButton.setOnClickListener {
            val newUsername = nameEditText.text.toString()

            // Validación del username
            if (newUsername.isEmpty()) {
                // Si el username está vacío, mostrar el error
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = "El nombre no puede estar vacío"
            } else {
                // Si es válido, ocultamos el error y actualizamos el nombre
                errorTextView.visibility = View.GONE

                // Actualizar el username en el objeto Usuario
                usuario.username = newUsername

                val db = FirebaseFirestore.getInstance()

                // Actualizar el username en el documento de usuario
                val userRef = db.collection("usuarios").document(usuario.id)

                userRef.update("username", newUsername)
                    .addOnSuccessListener {
                        // Actualizar el username en la lista de usuarios_asignados dentro del hogar
                        val hogarRef = db.collection("hogares").document(hogar.id)

                        // Buscar el documento de 'usuarios_asignados' donde el 'id_usuario' coincida
                        val usuariosAsignadosRef = hogarRef.collection("usuarios_asignados")

                        // Actualizar solo el campo "username" del documento correspondiente
                        usuariosAsignadosRef.whereEqualTo("id_usuario", usuario.id).get()
                            .addOnSuccessListener { querySnapshot ->
                                for (document in querySnapshot) {
                                    // Actualizar solo el campo "username" en el documento específico
                                    usuariosAsignadosRef.document(document.id).update("username", newUsername)
                                        .addOnSuccessListener {
                                            // Confirmación del éxito de la actualización
                                            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()

                                            // Redirigir a MenuActivity y cargar el fragmento "configuration"
                                            val intent = Intent(this, MenuActivity::class.java).apply {
                                                putExtra("hogar", hogar) // Pasa el hogar actualizado
                                                putExtra("usuario", usuario) // Pasa el usuario actualizado
                                                putExtra("initial_fragment", "configuration") // Especifica que se debe cargar el fragmento "configuration"
                                            }

                                            // Iniciar MenuActivity y finalizar esta actividad
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            errorTextView.visibility = View.VISIBLE
                                            errorTextView.text = "Error al actualizar el usuario asignado: ${e.message}"
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                errorTextView.visibility = View.VISIBLE
                                errorTextView.text = "Error al buscar usuario asignado: ${e.message}"
                            }
                    }
                    .addOnFailureListener { e ->
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.text = "Error al actualizar el usuario: ${e.message}"
                    }
            }
        }
    }
}