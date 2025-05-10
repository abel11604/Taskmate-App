package mx.edu.itson.taskmateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TareasAsignadasAdapter(private val tareas: List<TareaAsignada>) : RecyclerView.Adapter<TareasAsignadasAdapter.TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea_asignada, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tareaAsignada = tareas[position]
        holder.bind(tareaAsignada)
    }

    override fun getItemCount(): Int = tareas.size

    inner class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tareaTextView: TextView = itemView.findViewById(R.id.textoTarea)

        fun bind(tareaAsignada: TareaAsignada) {
            tareaTextView.text = tareaAsignada.tarea.nombreTarea
        }
    }
}