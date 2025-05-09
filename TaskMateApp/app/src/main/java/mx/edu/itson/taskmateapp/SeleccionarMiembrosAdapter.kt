package mx.edu.itson.taskmateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SeleccionarMiembrosAdapter(
    private val miembros: List<UsuarioAsignado>,
    private val onMemberSelected: (UsuarioAsignado) -> Unit
) : RecyclerView.Adapter<SeleccionarMiembrosAdapter.MiembroViewHolder>() {

    private val selectedMembers = mutableSetOf<UsuarioAsignado>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiembroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_miembro, parent, false)
        return MiembroViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiembroViewHolder, position: Int) {
        val miembro = miembros[position]
        holder.bind(miembro)
        holder.itemView.setOnClickListener {
            if (selectedMembers.contains(miembro)) {
                selectedMembers.remove(miembro)
                holder.itemView.setBackgroundResource(R.drawable.bg_miembro_default) // Estado no seleccionado
            } else {
                selectedMembers.add(miembro)
                holder.itemView.setBackgroundResource(R.drawable.bg_miembro_selected) // Estado seleccionado
            }
            onMemberSelected(miembro) // Pasar el miembro seleccionado
        }
    }

    override fun getItemCount(): Int = miembros.size

    // Cambié aquí la clase interna a MiembroViewHolder directamente en esta clase
    inner class MiembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.nombreMiembro)

        fun bind(miembro: UsuarioAsignado) {
            usernameTextView.text = miembro.username
        }
    }
}