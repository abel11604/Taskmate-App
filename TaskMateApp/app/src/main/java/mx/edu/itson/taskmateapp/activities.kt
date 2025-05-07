package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

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

        val createTaskButton: Button = view.findViewById(R.id.createTaskButton)

        val db = FirebaseFirestore.getInstance()

        usuario?.let { user ->
            db.collection("hogares").document(hogar!!.id).get()
                .addOnSuccessListener { doc ->
                    val datos = doc.data
                    if (datos != null) {
                        val tareasFirebase = datos["tareas"] as? List<Map<String, String>> ?: emptyList()
                        val tareasActualizadas = tareasFirebase.map {
                            Tarea(
                                id = it["id"] ?: "",
                                nombreTarea = it["nombreTarea"] ?: "Sin nombre",
                                descripcion = it["descripcion"] ?: ""
                            )
                        }.toMutableList()

                        val hogarActualizado = hogar!!.copy(tareas = tareasActualizadas)

                        val rolUsuario = hogarActualizado.usuariosAsignados
                            .firstOrNull { it.idUsuario == user.id }
                            ?.rol ?: "Habitante"

                        tasksAdapter = TasksHouseAdapter(
                            tasks = tareasActualizadas,
                            rolUsuario = rolUsuario,
                            onEditClick = { tarea, pos ->
                                val intent = Intent(activity, ActualizarTareaActivity::class.java).apply {
                                    putExtra("tarea", tarea)
                                    putExtra("hogar", hogarActualizado)
                                    putExtra("usuario", user)
                                }
                                startActivity(intent)
                            },
                            onDeleteClick = { tarea, pos ->
                                val nuevasTareas = tareasActualizadas.filter { it.id != tarea.id }
                                db.collection("hogares").document(hogar!!.id)
                                    .update("tareas", nuevasTareas.map {
                                        mapOf(
                                            "id" to it.id,
                                            "nombreTarea" to it.nombreTarea,
                                            "descripcion" to it.descripcion
                                        )
                                    })
                                    .addOnSuccessListener {
                                        tasksAdapter.removeTaskAt(pos)
                                        Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                                    }
                            }
                        )

                        recyclerView.adapter = tasksAdapter

                        if (rolUsuario != "Administrador") {
                            createTaskButton.visibility = View.GONE
                        } else {
                            createTaskButton.setOnClickListener {
                                val intent = Intent(activity, NuevaTareaCasaActivity::class.java).apply {
                                    putExtra("usuario", user)
                                    putExtra("hogar", hogarActualizado)
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }
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