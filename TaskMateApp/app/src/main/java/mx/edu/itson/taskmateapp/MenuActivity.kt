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

        // Obtener los parámetros del Intent
        val usuario = intent.getSerializableExtra("usuario") as? Usuario
        val hogar = intent.getSerializableExtra("hogar") as? Hogar
        val initialFragment = intent.getStringExtra("initial_fragment") ?: "inicio" // Valor por defecto "inicio"

        // Cargar el fragmento correspondiente
        if (usuario != null && hogar != null) {
            when (initialFragment) {
                "inicio" -> replaceFragment(inicio.newInstance(usuario, hogar))
                "activities" -> replaceFragment(activities.newInstance(usuario, hogar))
                "schedule" -> replaceFragment(schedule.newInstance(usuario, hogar))
                "members" -> replaceFragment(group.newInstance(usuario, hogar))
                "configuration" -> replaceFragment(configuration.newInstance(usuario, hogar))
                else -> replaceFragment(inicio.newInstance(usuario, hogar))  // En caso de que el String no coincida con ninguno
            }
        }

        // Configurar el listener para el menú de navegación
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}