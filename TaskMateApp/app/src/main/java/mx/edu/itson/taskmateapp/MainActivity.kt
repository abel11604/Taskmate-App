package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class MainActivity : AppCompatActivity() {
    private val list = mutableListOf<CarouselItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val carousel: ImageCarousel = findViewById(R.id.carousel)
        list.add(CarouselItem(R.drawable.panda1))
        list.add(CarouselItem(R.drawable.panda2))
        list.add(CarouselItem(R.drawable.panda3))

        carousel.addData(list)

        val loginButton: Button = findViewById(R.id.login_button)
        val registerSubtitle: TextView = findViewById(R.id.register_subtitle)


        loginButton.setOnClickListener {
            val intent = Intent(
                this,
                LoginActivity::class.java
            )  // Cambia LoginActivity por el nombre de tu actividad de login
            startActivity(intent)
        }


        registerSubtitle.setOnClickListener {
            val intent = Intent(
                this,
                SignInActivity::class.java
            )  // Cambia SignInActivity por el nombre de tu actividad de registro
            startActivity(intent)
        }
    }}