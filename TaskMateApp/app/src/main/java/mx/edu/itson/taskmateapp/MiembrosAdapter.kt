package mx.edu.itson.taskmateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class MiembrosAdapter(
    private val usuarios: List<UsuarioAsignado>,
    private val usuarioActual: Usuario,
    private val hogarActual: Hogar,
    private val onMiembroClick: (UsuarioAsignado) -> Unit
) : RecyclerView.Adapter<MiembrosAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.user_name)
        val rol = itemView.findViewById<TextView>(R.id.user_role)
        val avatar = itemView.findViewById<ImageView>(R.id.profile_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_miembro_hogar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = usuarios.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val miembro = usuarios[position]

        holder.nombre.text = miembro.username
        holder.rol.text = miembro.rol

        val esAdmin = hogarActual.usuariosAsignados.firstOrNull { it.idUsuario == usuarioActual.id }?.rol == "Administrador"
        val esElMismo = miembro.idUsuario == usuarioActual.id

        if (esAdmin && !esElMismo) {
            holder.itemView.setOnClickListener {
                onMiembroClick(miembro)
            }
        } else {
            holder.itemView.setOnClickListener(null)
        }
    }
}