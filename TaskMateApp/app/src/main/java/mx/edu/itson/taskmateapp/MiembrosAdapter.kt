package mx.edu.itson.taskmateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MiembrosAdapter(private val miembros: List<UsuarioAsignado>) :
    RecyclerView.Adapter<MiembrosAdapter.MiembroViewHolder>() {

    inner class MiembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenPerfil: ImageView = itemView.findViewById(R.id.profile_image)
        val nombreUsuario: TextView = itemView.findViewById(R.id.user_name)
        val rolUsuario: TextView = itemView.findViewById(R.id.user_role)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiembroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_miembro_hogar, parent, false)
        return MiembroViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiembroViewHolder, position: Int) {
        val miembro = miembros[position]
        holder.nombreUsuario.text = miembro.username
        holder.rolUsuario.text = miembro.rol


        holder.imagenPerfil.setImageResource(R.drawable.account_circle)
    }

    override fun getItemCount(): Int = miembros.size
}