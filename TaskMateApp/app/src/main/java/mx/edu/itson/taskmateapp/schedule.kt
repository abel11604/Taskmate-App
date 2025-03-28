package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [schedule.newInstance] factory method to
 * create an instance of this fragment.
 */
class schedule : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_schedule, container, false)

        // Obtener las vistas
        val taskTextView: LinearLayout = rootView.findViewById(R.id.layoutTarea) // Cambiar ID si es diferente
        val addButton: ImageView = rootView.findViewById(R.id.fab_add_task)

        // Agregar el OnClickListener para el TextView de la tarea
        taskTextView.setOnClickListener {
            // Crear la intención para abrir la actividad AgregarTareaActivity
            val intent = Intent(activity, AgregarTareaActivity::class.java)
            startActivity(intent)
        }

        // Agregar el OnClickListener para el ícono de agregar tarea
        addButton.setOnClickListener {
            // Crear la intención para abrir la actividad AgregarTareaActivity
            val intent = Intent(activity, AgregarTareaActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment schedule.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            schedule().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}