package mx.edu.itson.taskmateapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

class AgregarTareaActivity : AppCompatActivity() {

    private lateinit var usuario: Usuario
    private lateinit var hogar: Hogar

    private lateinit var horaSeleccionadaTextView: TextView
    private lateinit var fechaSeleccionadaTextView: TextView
    private lateinit var tareasRecyclerView: RecyclerView
    private lateinit var miembrosRecyclerView: RecyclerView

    private lateinit var tareasAdapter: TareasAdapter
    private lateinit var miembrosAdapter: SeleccionarMiembrosAdapter

    private var usuariosSeleccionados = mutableListOf<UsuarioAsignado>()
    private var tareaSeleccionada: Tarea? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tarea)

        // Obtener el hogar y el usuario desde el intent
        usuario = intent.getSerializableExtra("usuario") as Usuario
        hogar = intent.getSerializableExtra("hogar") as Hogar

        // Referencias a los elementos del layout
        horaSeleccionadaTextView = findViewById(R.id.horaSeleccionada)
        fechaSeleccionadaTextView = findViewById(R.id.fechaSeleccionada)
        tareasRecyclerView = findViewById(R.id.tareasRecyclerView)
        miembrosRecyclerView = findViewById(R.id.miembrosRecyclerView)

        // Inicializar los RecyclerViews
        tareasRecyclerView.layoutManager = LinearLayoutManager(this)
        miembrosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Cargar tareas y miembros
        cargarTareas()
        cargarMiembros()

        // Configuración del selector de hora
        horaSeleccionadaTextView.setOnClickListener {
            showTimePickerDialog()
        }

        // Configuración del selector de fecha
        fechaSeleccionadaTextView.setOnClickListener {
            showDatePickerDialog()
        }

        // Configuración de la programación de la tarea
        val programarTareaButton: Button = findViewById(R.id.programarTareaButton)
        programarTareaButton.setOnClickListener {
            guardarTarea()
        }
    }

    private fun cargarTareas() {
        // Aquí se carga las tareas del hogar, por ejemplo, las tareas del hogar pasado en el objeto `hogar`
        val tareas = hogar.tareas

        // Configurar el adapter de tareas
        tareasAdapter = TareasAdapter(tareas) { tarea ->
            tareaSeleccionada = tarea
        }
        tareasRecyclerView.adapter = tareasAdapter
    }

    private fun cargarMiembros() {
        // Aquí se carga los miembros asignados al hogar
        val miembros = hogar.usuariosAsignados

        // Configurar el adapter de miembros
        miembrosAdapter = SeleccionarMiembrosAdapter(miembros) { miembro ->
            if (usuariosSeleccionados.contains(miembro)) {
                usuariosSeleccionados.remove(miembro)
            } else {
                usuariosSeleccionados.add(miembro)
            }
            // Actualizar el fondo del miembro seleccionado
            miembrosAdapter.notifyDataSetChanged()
        }
        miembrosRecyclerView.adapter = miembrosAdapter
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                fechaSeleccionadaTextView.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                horaSeleccionadaTextView.text = formattedTime
            },
            8, 0, true // Hora por defecto (8:00 AM)
        )
        timePickerDialog.show()
    }

    private fun guardarTarea() {
        // Si no se ha seleccionado una tarea o los miembros, mostramos un mensaje
        if (tareaSeleccionada == null || usuariosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una tarea y miembros.", Toast.LENGTH_SHORT).show()
            return
        }

        // Asegurarse de que la fecha y la hora seleccionadas no sean nulas
        val selectedDate = fechaSeleccionadaTextView.text.toString() // "22/5/2025"
        val selectedTime = horaSeleccionadaTextView.text.toString() // "11:16"

        if (selectedDate.isBlank() || selectedTime.isBlank()) {
            Toast.makeText(this, "Por favor, selecciona una fecha y hora válidas.", Toast.LENGTH_SHORT).show()
            return
        }

        // Parsear la fecha y hora seleccionadas
        val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm") // "22/5/2025 11:16"
        val fechaHora = LocalDateTime.parse("$selectedDate $selectedTime", dateFormatter)

        // Crear la tarea asignada con los miembros seleccionados y la fecha y hora seleccionadas
        val tareaAsignada = TareaAsignada(
            id = UUID.randomUUID().toString(),
            tarea = tareaSeleccionada!!,
            usuariosAsignados = usuariosSeleccionados,
            horaRealizacion = fechaHora, // Usar la hora seleccionada
            estado = "Pendiente" // O el estado que decidas
        )

        // Actualizar la lista de tareasAsignadas en el hogar
        val db = FirebaseFirestore.getInstance()
        db.collection("hogares")
            .document(hogar.id)
            .update("tareasAsignadas", FieldValue.arrayUnion(tareaAsignada))
            .addOnSuccessListener {
                Toast.makeText(this, "Tarea programada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al programar la tarea: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}