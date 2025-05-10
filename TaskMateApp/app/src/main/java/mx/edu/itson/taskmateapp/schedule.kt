package mx.edu.itson.taskmateapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USUARIO = "usuario"
private const val ARG_HOGAR = "hogar"

/**
 * A simple [Fragment] subclass.
 * Use the [schedule.newInstance] factory method to
 * create an instance of this fragment.
 */
class schedule : Fragment() {

    private var usuario: Usuario? = null
    private var hogar: Hogar? = null

    private lateinit var tareasAsignadasAdapter: TareasAsignadasAdapter // Adaptador para las tareas asignadas
    private lateinit var recyclerTareasProgramadas: RecyclerView
    private lateinit var addButton: ImageView // Aquí guardamos el botón

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
        val rootView = inflater.inflate(R.layout.fragment_schedule, container, false)

        val fechaSeleccionadaTextView = rootView.findViewById<TextView>(R.id.fechaSeleccionadaTextView)
        recyclerTareasProgramadas = rootView.findViewById(R.id.recyclerTareasProgramadas)
        addButton = rootView.findViewById(R.id.fab_add_task)

        // Formato de la fecha seleccionada
        val calendario = Calendar.getInstance()
        val formato = SimpleDateFormat("EEE dd MMM", Locale("es", "MX"))
        fechaSeleccionadaTextView.text = formato.format(calendario.time)

        // Seleccionar fecha
        fechaSeleccionadaTextView.setOnClickListener {
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialog,
                { _, y, m, d ->
                    calendario.set(y, m, d)
                    fechaSeleccionadaTextView.text = formato.format(calendario.time)
                    cargarTareasParaFecha(calendario.time)  // Filtrar tareas por fecha
                },
                year, month, day
            )
            datePicker.show()
        }

        // Inicializar el RecyclerView
        recyclerTareasProgramadas.layoutManager = LinearLayoutManager(requireContext())

        // Lógica para mostrar el botón solo a los moderadores y administradores
        if (usuario != null && hogar != null) {
            // Verificar si el usuario tiene el rol de Administrador o Moderador en los usuarios asignados
            val usuarioAsignado = hogar?.usuariosAsignados?.find { it.idUsuario == usuario?.id }

            if (usuarioAsignado != null && (usuarioAsignado.rol == "Administrador" || usuarioAsignado.rol == "Moderador")) {
                addButton.visibility = View.VISIBLE
            } else {
                addButton.visibility = View.GONE
            }
        }

        // Configuración del evento del botón
        addButton.setOnClickListener {
            // Aquí manejas el evento para agregar tarea
            val intent = Intent(activity, AgregarTareaActivity::class.java)
            intent.putExtra("hogar", hogar)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        return rootView
    }

    private fun cargarTareasParaFecha(fecha: Date) {
        // Convertir la fecha seleccionada a LocalDate
        val selectedDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        // Filtrar tareas del hogar que coincidan con la fecha seleccionada
        val tareas = hogar?.tareasAsignadas?.filter {
            // Convertir la fecha de la tarea a LocalDate solo tomando el día, mes y año
            val tareaFecha = it.horaRealizacion.toLocalDate()

            // Log para depurar las fechas
            Log.d("Tareas", "Tarea fecha: $tareaFecha, Fecha seleccionada: $selectedDate")

            // Comparar las fechas sin considerar horas ni minutos
            tareaFecha == selectedDate
        } ?: emptyList()

        // Si no hay tareas asignadas para esa fecha, muestra un mensaje o agrega lógica
        if (tareas.isEmpty()) {
            Log.d("Tareas", "No hay tareas para la fecha seleccionada.")
        }

        // Configurar el adaptador con las tareas filtradas
        tareasAsignadasAdapter = TareasAsignadasAdapter(tareas)
        recyclerTareasProgramadas.adapter = tareasAsignadasAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(usuario: Usuario, hogar: Hogar) =
            schedule().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USUARIO, usuario)
                    putSerializable(ARG_HOGAR, hogar)
                }
            }
    }
}