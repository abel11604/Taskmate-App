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



        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.page_inicio -> replaceFragment(inicio())
                R.id.page_activities -> replaceFragment(activities())
                R.id.page_schedule -> replaceFragment(schedule())
                R.id.page_members -> replaceFragment(group())
                R.id.page_configuration -> replaceFragment(configuration())

                else ->{


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