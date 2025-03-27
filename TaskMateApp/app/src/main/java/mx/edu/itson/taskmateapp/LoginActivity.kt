package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
      val logInBtn: Button = findViewById(R.id.login_button)
        val registerSubtitle: TextView = findViewById(R.id.register_subtitle)
        logInBtn.setOnClickListener {
            val intent = Intent(
                this,
                InicioActivity::class.java
            )
            startActivity(intent)
        }

        registerSubtitle.setOnClickListener {
            val intent = Intent(
                this,
                SignInActivity::class.java
            )  // Cambia SignInActivity por el nombre de tu actividad de registro
            startActivity(intent)
        }
    }
}