package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

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
            val usuariosAsignadosRaw = hogarActual.usuariosAsignados

            // Crear lista de usuarios completos
            val usuariosList = mutableListOf<UsuarioAsignado>()

            // Obtener los detalles completos de cada usuario desde Firestore
            val db = FirebaseFirestore.getInstance()
            val usuariosRef = db.collection("usuarios")

            // Realizar la consulta para obtener los datos completos de los usuarios
            usuariosAsignadosRaw.forEach { asignado ->
                val idUsuario = asignado.idUsuario
                usuariosRef.document(idUsuario).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val username = document.getString("username") ?: "Desconocido"
                            val correo = document.getString("correo") ?: "Desconocido"

                            val usuarioAsignado = UsuarioAsignado(
                                idUsuario = idUsuario,
                                rol = asignado.rol,
                                username = username,
                                correo = correo
                            )

                            // Añadir usuario completo a la lista
                            usuariosList.add(usuarioAsignado)

                            // Cuando se haya recuperado toda la información, actualizamos el RecyclerView
                            if (usuariosList.size == usuariosAsignadosRaw.size) {
                                recyclerView.adapter = MiembrosAdapter(usuariosList)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("group", "Error obteniendo usuario: ", exception)
                    }
            }
        }

        val nombreHogarTv = view.findViewById<TextView>(R.id.nombreHogarTv)
        nombreHogarTv.text = hogar?.nombreHogar ?: "Nombre no disponible"
        val codigoHogarTv=view.findViewById<TextView>(R.id.idHogarTv)
        codigoHogarTv.text=hogar?.accesoCodigo?:"no"

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