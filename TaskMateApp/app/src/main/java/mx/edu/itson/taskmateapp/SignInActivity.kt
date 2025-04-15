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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        val userEt = findViewById<EditText>(R.id.userEt)
        val emailEt = findViewById<EditText>(R.id.emailEt)
        val passwordEt = findViewById<EditText>(R.id.passwordEt)
        val confirmPasswordEt = findViewById<EditText>(R.id.confirm_password)
        val registerBtn = findViewById<Button>(R.id.register_button)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)
        val loginTV = findViewById<TextView>(R.id.register_subtitle)

        loginTV.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerBtn.setOnClickListener {
            errorTextView.visibility = View.GONE

            val username = userEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()
            val confirmPassword = confirmPasswordEt.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                errorTextView.text = "Todos los campos son obligatorios"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                errorTextView.text = "Las contraseñas no coinciden"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        if (firebaseUser != null) {


                            val userData = hashMapOf(
                                "username" to username,
                                "correo" to email,

                            )


                            db.collection("usuarios")
                                .document(firebaseUser.uid)
                                .set(userData)
                                .addOnSuccessListener {

                                    val usuario = Usuario(
                                        id = firebaseUser.uid,
                                        username = username,
                                        correo = email,
                                        password = "" // No se guarda la contraseña
                                    )
                                    val intent = Intent(this, InicioActivity::class.java)
                                    intent.putExtra("usuario", usuario)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    errorTextView.text = "Error al guardar datos: ${e.message}"
                                    errorTextView.visibility = View.VISIBLE
                                }
                        }
                    } else {
                        errorTextView.text = "Error: ${task.exception?.message}"
                        errorTextView.visibility = View.VISIBLE
                    }
                }
        }
    }
}