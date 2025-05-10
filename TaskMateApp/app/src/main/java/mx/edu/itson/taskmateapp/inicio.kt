package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


private const val ARG_USUARIO = "usuario"
private const val ARG_HOGAR = "hogar"

class inicio : Fragment() {

    private var usuario: Usuario? = null
    private var hogar: Hogar? = null
    private val db = Firebase.firestore
    private lateinit var taskAdapter: TaskAdapter // Adaptador para las tareas
    private lateinit var recyclerViewTareas: RecyclerView
    private lateinit var fechaSeleccionadaTextView: TextView // Asegúrate de declararlo aquí correctamente
    private var tareasDelDia: MutableList<TareaAsignada> = mutableListOf()

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
        val rootView = inflater.inflate(R.layout.fragment_inicio, container, false)

        val bienvenida: TextView = rootView.findViewById(R.id.greetingTextView)

        bienvenida.text = "Hola ${usuario?.username ?: " "}"

        // Aquí inicializamos correctamente la vista
        fechaSeleccionadaTextView = rootView.findViewById(R.id.dia)
        recyclerViewTareas = rootView.findViewById(R.id.rvTareas)

        // Inicialización del RecyclerView
        recyclerViewTareas.layoutManager = LinearLayoutManager(requireContext())

        // Configuración de los botones de los días
        rootView.findViewById<MaterialButton>(R.id.btnLun).setOnClickListener { seleccionarDia("Lunes") }
        rootView.findViewById<MaterialButton>(R.id.btnMar).setOnClickListener { seleccionarDia("Martes") }
        rootView.findViewById<MaterialButton>(R.id.btnMie).setOnClickListener { seleccionarDia("Miércoles") }
        rootView.findViewById<MaterialButton>(R.id.btnJue).setOnClickListener { seleccionarDia("Jueves") }
        rootView.findViewById<MaterialButton>(R.id.btnVie).setOnClickListener { seleccionarDia("Viernes") }
        rootView.findViewById<MaterialButton>(R.id.btnSab).setOnClickListener { seleccionarDia("Sábado") }
        rootView.findViewById<MaterialButton>(R.id.btnDom).setOnClickListener { seleccionarDia("Domingo") }

        return rootView
    }

    // Función para seleccionar el día y cargar las tareas correspondientes
    private fun seleccionarDia(dia: String) {
        fechaSeleccionadaTextView.text = dia
        cargarTareasDelDia(dia)
    }

    // Cargar las tareas del día seleccionado
// Cargar las tareas del día seleccionado
    private fun cargarTareasDelDia(dia: String) {
        // Crear un mapa que asocie los días en español con los días en inglés
        val diasEnIngles = mapOf(
            "Lunes" to "MONDAY",
            "Martes" to "TUESDAY",
            "Miércoles" to "WEDNESDAY",
            "Jueves" to "THURSDAY",
            "Viernes" to "FRIDAY",
            "Sábado" to "SATURDAY",
            "Domingo" to "SUNDAY"
        )

        // Log: Imprimir todo el contenido de tareasAsignadas para ver qué tienes
        Log.d("Tareas", "Tareas asignadas del hogar: ${hogar?.tareasAsignadas}")

        // Obtener todas las tareas asignadas que sean del usuario y que estén en estado "Pendiente"
        tareasDelDia = hogar?.tareasAsignadas?.filter { tareaAsignada ->
            val tareaFecha = tareaAsignada.horaRealizacion.dayOfWeek.toString() // Día de la tarea en inglés


            // Log: Verificar qué tiene `horaRealizacion` para cada tarea
            Log.d("Tareas", "Tarea: ${tareaAsignada.tarea.nombreTarea}, Fecha: ${tareaAsignada.horaRealizacion.dayOfWeek}, Estado: ${tareaAsignada.estado}")

            // Convertir el día de la tarea (en inglés) al día seleccionado (en español)
            val diaTareaEnEspanol = diasEnIngles[dia] // Mapeamos el día de español a inglés

            // Filtrar tareas que coincidan con el día seleccionado y estén pendientes
            diaTareaEnEspanol == tareaFecha != null && tareaAsignada.estado == "Pendiente"
        }?.toMutableList() ?: mutableListOf()  // Convertir a MutableList

        // Log: Mostrar las tareas filtradas
        Log.d("Tareas", "Tareas filtradas para el día $dia: $tareasDelDia")

        // Configurar el adaptador con las tareas filtradas
        taskAdapter = TaskAdapter(tareasDelDia) { tarea ->
            // Cuando se marca la tarea como realizada, se actualiza en Firebase y se elimina de la vista
            actualizarTareaRealizada(tarea)
        }
        recyclerViewTareas.adapter = taskAdapter
    }

    // Actualizar la tarea en Firebase como "Realizada"
    private fun actualizarTareaRealizada(tarea: TareaAsignada) {
        val tareaIndex = hogar?.tareasAsignadas?.indexOf(tarea)
        tareaIndex?.let { index ->
            val tareaActualizada = tarea.copy(estado = "Realizada")
            val tareasActualizadas = hogar?.tareasAsignadas?.toMutableList()
            tareasActualizadas?.set(index, tareaActualizada)

            // Actualizar en Firebase
            hogar?.id?.let {
                db.collection("hogares")
                    .document(it)
                    .update("tareasAsignadas", tareasActualizadas)
                    .addOnSuccessListener {
                        // Eliminar la tarea de la vista
                        (recyclerViewTareas.adapter as? TaskAdapter)?.removeTask(tarea)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al actualizar la tarea", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(usuario: Usuario, hogar: Hogar) =
            inicio().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USUARIO, usuario)
                    putSerializable(ARG_HOGAR, hogar)
                }
            }
    }
}