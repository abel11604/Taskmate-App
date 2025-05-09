package mx.edu.itson.taskmateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TareasAdapter(
    private val tareas: List<Tarea>,
    private val onTaskSelected: (Tarea) -> Unit
) : RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {

    private var selectedTask: Tarea? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.bind(tarea)

        // Se establece el estado del RadioButton dependiendo si la tarea está seleccionada
        holder.radioSeleccion.isChecked = selectedTask == tarea

        // Cuando se hace clic en una tarea
        holder.itemView.setOnClickListener {
            // Si ya estaba seleccionada, la deseleccionamos
            if (selectedTask == tarea) {
                selectedTask = null
                holder.radioSeleccion.isChecked = false
            } else {
                // Si no estaba seleccionada, la seleccionamos
                selectedTask = tarea
                holder.radioSeleccion.isChecked = true
            }
            // Llamamos a la función de onTaskSelected con la tarea seleccionada
            onTaskSelected(tarea)
            // Notificamos al adaptador que se actualizó la selección
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = tareas.size

    inner class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTarea: TextView = itemView.findViewById(R.id.nombreTarea)
        val radioSeleccion: RadioButton = itemView.findViewById(R.id.radioSeleccion)

        fun bind(tarea: Tarea) {
            nombreTarea.text = tarea.nombreTarea
        }
    }
}