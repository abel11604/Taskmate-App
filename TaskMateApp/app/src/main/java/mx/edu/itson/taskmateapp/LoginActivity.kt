package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    // Instancia de Firestore
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val emailEt = findViewById<EditText>(R.id.email_input)
        val passwordEt = findViewById<EditText>(R.id.password_input)
        val loginBtn = findViewById<Button>(R.id.login_button)
        val registerSubtitle = findViewById<TextView>(R.id.register_subtitle)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)

        loginBtn.setOnClickListener {
            errorTextView.visibility = View.GONE

            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()){
                errorTextView.text = "Por favor, completa todos los campos"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val firebaseUser = auth.currentUser
                        if (firebaseUser != null) {

                            db.collection("usuarios")
                                .document(firebaseUser.uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {

                                        val username = document.getString("username") ?: ""
                                        val correo = firebaseUser.email ?: ""


                                        val usuario = Usuario(
                                            id = firebaseUser.uid,
                                            username = username,
                                            correo = correo,
                                            password = ""
                                        )


                                        val intent = Intent(this, InicioActivity::class.java)
                                        intent.putExtra("usuario", usuario)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        errorTextView.text = "No se encontraron datos del usuario."
                                        errorTextView.visibility = View.VISIBLE
                                    }
                                }
                                .addOnFailureListener { e ->
                                    errorTextView.text = "Error al obtener datos: ${e.message}"
                                    errorTextView.visibility = View.VISIBLE
                                }
                        }
                    } else {
                        errorTextView.text = "Correo o contraseña incorrectos"
                        errorTextView.visibility = View.VISIBLE
                    }
                }
        }

        registerSubtitle.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}