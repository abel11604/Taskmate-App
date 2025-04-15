package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)


        val usuario = intent.getSerializableExtra("usuario") as? Usuario


        val userTextView = findViewById<TextView>(R.id.user)

        if (usuario != null) {
            userTextView.text = usuario.username
        } else {
            userTextView.text = "Usuario"
        }

        val btnUnirseHogar: LinearLayout = findViewById(R.id.btn_unirse_hogar)
        val btnRegistrarHogar: LinearLayout = findViewById(R.id.btn_registar_hogar)

        btnUnirseHogar.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        btnRegistrarHogar.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}