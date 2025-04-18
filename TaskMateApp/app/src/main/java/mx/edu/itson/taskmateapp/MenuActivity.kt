package mx.edu.itson.taskmateapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import mx.edu.itson.taskmateapp.databinding.ActivityMainBinding
import mx.edu.itson.taskmateapp.databinding.ActivityMenuBinding


class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(inicio())

        val usuario = intent.getSerializableExtra("usuario") as? Usuario
        val hogar = intent.getSerializableExtra("hogar") as? Hogar
     //aqui ps dependiendo de que boton del menu piques se va a meter ese fragment al layout
        binding.bottomNavigationView.setOnItemSelectedListener {

            if (usuario != null && hogar != null) {
                when (it.itemId) {
                    R.id.page_inicio -> replaceFragment(inicio.newInstance(usuario, hogar))
                    R.id.page_activities -> replaceFragment(activities.newInstance(usuario, hogar))
                    R.id.page_schedule -> replaceFragment(schedule.newInstance(usuario, hogar))
                    R.id.page_members -> replaceFragment(group.newInstance(usuario, hogar))
                    R.id.page_configuration -> replaceFragment(configuration.newInstance(usuario, hogar))
                    else -> {}
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}