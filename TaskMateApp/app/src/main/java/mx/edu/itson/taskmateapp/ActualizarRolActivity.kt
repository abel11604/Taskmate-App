package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class ActualizarRolActivity : AppCompatActivity() {

    private lateinit var usuario: Usuario
    private lateinit var hogar: Hogar
    private lateinit var usuarioSeleccionado: UsuarioAsignado

    private lateinit var switchRol: Switch
    private lateinit var btnActualizarRol: Button
    private lateinit var nombreUsuario: TextView
    private lateinit var backButton: ImageView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_rol)

        usuario = intent.getSerializableExtra("usuario") as Usuario
        hogar = intent.getSerializableExtra("hogar") as Hogar
        usuarioSeleccionado = intent.getSerializableExtra("usuarioSeleccionado") as UsuarioAsignado

        switchRol = findViewById(R.id.switchRol)
        btnActualizarRol = findViewById(R.id.btnActualizarRol)
        nombreUsuario = findViewById(R.id.userNameTextView)
        backButton = findViewById(R.id.backButton)

        nombreUsuario.text = usuarioSeleccionado.username

        // Estado actual
        switchRol.isChecked = usuarioSeleccionado.rol == "Moderador"

        backButton.setOnClickListener { finish() }

        btnActualizarRol.setOnClickListener {
            val nuevoRol = if (switchRol.isChecked) "Moderador" else "Habitante"

            val nuevosUsuariosAsignados = hogar.usuariosAsignados.map {
                if (it.idUsuario == usuarioSeleccionado.idUsuario) {
                    it.copy(rol = nuevoRol)
                } else it
            }

            db.collection("hogares").document(hogar.id)
                .update("usuarios_asignados", nuevosUsuariosAsignados.map {
                    mapOf(
                        "id_usuario" to it.idUsuario,
                        "rol" to it.rol
                    )
                })
                .addOnSuccessListener {
                    Toast.makeText(this, "Rol actualizado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuActivity::class.java).apply {
                        putExtra("hogar", hogar.copy(usuariosAsignados = nuevosUsuariosAsignados))
                        putExtra("usuario", intent.getSerializableExtra("usuario"))
                    }
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}