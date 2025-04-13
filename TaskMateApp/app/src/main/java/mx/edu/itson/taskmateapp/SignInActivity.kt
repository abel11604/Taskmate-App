package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
     val signInBtn:Button=findViewById(R.id.register_button)
        signInBtn.setOnClickListener {
            val intent = Intent(
                this,
                InicioActivity::class.java
            )
            startActivity(intent)

        }
    }

}