package mx.edu.itson.taskmateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class TaskAdapter(
    private val tareasDelDia: MutableList<TareaAsignada>,
    private val onTaskSelected: (TareaAsignada) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tareas_pendientes_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tarea = tareasDelDia[position]
        holder.bind(tarea)

        holder.itemView.setOnClickListener {
            if (tarea.estado == "Pendiente") {
                tarea.estado = "Realizada"
                notifyItemChanged(position)
                onTaskSelected(tarea)
            }
        }
    }

    override fun getItemCount(): Int = tareasDelDia.size

    fun removeTask(tarea: TareaAsignada) {
        val position = tareasDelDia.indexOf(tarea)
        if (position != -1) {
            tareasDelDia.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTareaTextView: TextView = itemView.findViewById(R.id.tvTaskName)
        private val horaTareaTextView: TextView = itemView.findViewById(R.id.tvHoraTarea)
        private val palomita: ImageView = itemView.findViewById(R.id.checkImage)

        private val horaFormatter = DateTimeFormatter.ofPattern("HH:mm")  // Formato deseado

        fun bind(tarea: TareaAsignada) {
            nombreTareaTextView.text = tarea.tarea.nombreTarea
            horaTareaTextView.text = tarea.horaRealizacion.toLocalTime().format(horaFormatter)
            palomita.visibility = if (tarea.estado == "Pendiente") View.VISIBLE else View.GONE
        }
    }
}