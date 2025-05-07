package mx.edu.itson.taskmateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksHouseAdapter(
    private val tasks: MutableList<Tarea>,
    private val rolUsuario: String,
    private val onEditClick: (Tarea, Int) -> Unit,
    private val onDeleteClick: (Tarea, Int) -> Unit
) : RecyclerView.Adapter<TasksHouseAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val editButton: ImageView = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tareas_casa_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tarea = tasks[position]

        holder.taskName.text = tarea.nombreTarea
        holder.taskDescription.text = tarea.descripcion

        holder.taskName.setOnClickListener {
            holder.taskDescription.visibility =
                if (holder.taskDescription.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        if (rolUsuario == "Administrador" || rolUsuario == "Moderador") {
            holder.editButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE

            holder.editButton.setOnClickListener {
                onEditClick(tarea, position)
            }

            holder.deleteButton.setOnClickListener {
                onDeleteClick(tarea, position)
            }
        } else {
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int = tasks.size

    fun removeTaskAt(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addTask(tarea: Tarea) {
        tasks.add(tarea)
        notifyItemInserted(tasks.size - 1)
    }
}
