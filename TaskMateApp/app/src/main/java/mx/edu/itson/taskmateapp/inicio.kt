package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [inicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class inicio : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_inicio, container, false)


        val historialBtn: TextView = rootView.findViewById(R.id.btn_ver_historial)
        historialBtn.setOnClickListener {
            val intent = Intent(activity, HistorialActivity::class.java)
            startActivity(intent)
        }


        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvTareas)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Datos de prueba para las tareas
        val taskList = listOf(
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Hacer comida", "Descripción de hacer comida"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),


        )

        val adapter = TaskAdapter(taskList)
        recyclerView.adapter = adapter

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment inicio.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            inicio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}