package mx.edu.itson.taskmateapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HogarAdapter(
    private val hogares: List<Hogar>,
    private val usuario: Usuario,
    private val onClick: (Hogar) -> Unit
) : RecyclerView.Adapter<HogarAdapter.HogarViewHolder>() {

    inner class HogarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreHogarText)
        val botonEntrar: Button = itemView.findViewById(R.id.btnSeleccionarHogar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HogarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hogar_usuario, parent, false)
        return HogarViewHolder(view)
    }

    override fun onBindViewHolder(holder: HogarViewHolder, position: Int) {
        val hogar = hogares[position]
        holder.nombreTextView.text = hogar.nombreHogar
        holder.botonEntrar.setOnClickListener {
            val intent = Intent(holder.itemView.context, MenuActivity::class.java).apply {
                putExtra("usuario", usuario)
                putExtra("hogar", hogar)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = hogares.size
}