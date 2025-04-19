package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USUARIO = "usuario"
private const val ARG_HOGAR = "hogar"

/**
 * A simple [Fragment] subclass.
 * Use the [group.newInstance] factory method to
 * create an instance of this fragment.
 */
class group : Fragment() {

    private var usuario: Usuario? = null
    private var hogar: Hogar? = null

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
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.miembrosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        hogar?.let { hogarActual ->
            val miembros = hogarActual.usuariosAsignados
            recyclerView.adapter = MiembrosAdapter(miembros)
        }

        val nombreHogarTv = view.findViewById<TextView>(R.id.nombreHogarTv)
        nombreHogarTv.text = hogar?.nombreHogar ?: "Nombre no disponible"
    }

    companion object {
        @JvmStatic
        fun newInstance(usuario: Usuario, hogar: Hogar) =
            group().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USUARIO, usuario)
                    putSerializable(ARG_HOGAR, hogar)
                }
            }
    }
}