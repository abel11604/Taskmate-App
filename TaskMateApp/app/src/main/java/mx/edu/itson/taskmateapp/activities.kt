package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [activities.newInstance] factory method to
 * create an instance of this fragment.
 */
class activities : Fragment() {
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
        // Inflar el layout para este fragmento
        val rootView = inflater.inflate(R.layout.fragment_activities, container, false)

        // Referencias de los ImageView para editar (asume que los IDs son correctos)
        val editTask1: ImageView = rootView.findViewById(R.id.imageView4)
        val editTask2: ImageView = rootView.findViewById(R.id.imageView8)
        val editTask3: ImageView = rootView.findViewById(R.id.imageView12)
        val editTask4: ImageView = rootView.findViewById(R.id.imageView13)

        // Asignar OnClickListener para cada ImageView (para editar tarea)
        editTask1.setOnClickListener {
            val intent = Intent(activity, ActualizarTareaActivity::class.java)
            startActivity(intent)
        }
        editTask2.setOnClickListener {
            val intent = Intent(activity, ActualizarTareaActivity::class.java)
            startActivity(intent)
        }
        editTask3.setOnClickListener {
            val intent = Intent(activity, ActualizarTareaActivity::class.java)
            startActivity(intent)
        }
        editTask4.setOnClickListener {
            val intent = Intent(activity, ActualizarTareaActivity::class.java)
            startActivity(intent)
        }

        // Referencia del botón de crear tarea
        val createTaskButton: Button = rootView.findViewById(R.id.createTaskButton)

        // Asignar OnClickListener para el botón de crear tarea
        createTaskButton.setOnClickListener {
            val intent = Intent(activity, ActualizarTareaActivity::class.java)
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
         * @return A new instance of fragment activities.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            activities().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}