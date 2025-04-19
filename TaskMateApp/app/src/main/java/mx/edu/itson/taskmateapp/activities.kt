package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USUARIO = "usuario"
private const val ARG_HOGAR = "hogar"

/**
 * A simple [Fragment] subclass.
 * Use the [activities.newInstance] factory method to
 * create an instance of this fragment.
 */

class activities : Fragment() {
    private var usuario: Usuario? = null
    private var hogar: Hogar? = null
    private lateinit var tasksAdapter: TasksHouseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuario = it.getSerializable(ARG_USUARIO) as? Usuario
            hogar = it.getSerializable(ARG_HOGAR) as? Hogar
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)


        val listOfTasks = hogar
            ?.tareas
            ?.map { Tarea("", it.nombreTarea, it.descripcion) }
            ?.toMutableList()
            ?: mutableListOf()


        val rolUsuario = hogar?.usuariosAsignados
            ?.firstOrNull { it.idUsuario == usuario?.id }
            ?.rol ?: "Habitante"


        tasksAdapter = TasksHouseAdapter(
            tasks = listOfTasks,
            rolUsuario = rolUsuario,
            onEditClick = { tarea, pos ->
                val intent = Intent(activity, ActualizarTareaActivity::class.java).apply {
                    putExtra("tarea", tarea)
                }
                startActivity(intent)
            },
            onDeleteClick = { tarea, pos ->
                tasksAdapter.removeTaskAt(pos)
            }
        )

        recyclerView.adapter = tasksAdapter


        val createTaskButton: Button = view.findViewById(R.id.createTaskButton)
        createTaskButton.setOnClickListener {
            val intent = Intent(activity, NuevaTareaCasaActivity::class.java)
            intent.putExtra("usuario", usuario)
            intent.putExtra("hogar", hogar)
            startActivity(intent)
        }


        if (rolUsuario != "Administrador") {
            createTaskButton.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(usuario: Usuario, hogar: Hogar) =
            activities().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USUARIO, usuario)
                    putSerializable(ARG_HOGAR, hogar)
                }
            }
    }
}