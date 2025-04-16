package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton


private const val ARG_USUARIO = "usuario"
private const val ARG_HOGAR = "hogar"

class inicio : Fragment() {

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

        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bienvenida: TextView = view.findViewById(R.id.greetingTextView)
        bienvenida.text = "Hola " + (usuario?.username ?: " ")

        val diaTextView: TextView = view.findViewById(R.id.dia)


        val btnLun = view.findViewById<MaterialButton>(R.id.btnLun)
        val btnMar = view.findViewById<MaterialButton>(R.id.btnMar)
        val btnMie = view.findViewById<MaterialButton>(R.id.btnMie)
        val btnJue = view.findViewById<MaterialButton>(R.id.btnJue)
        val btnVie = view.findViewById<MaterialButton>(R.id.btnVie)
        val btnSab = view.findViewById<MaterialButton>(R.id.btnSab)
        val btnDom = view.findViewById<MaterialButton>(R.id.btnDom)


        btnLun.setOnClickListener { diaTextView.text = "Lunes" }
        btnMar.setOnClickListener { diaTextView.text = "Martes" }
        btnMie.setOnClickListener { diaTextView.text = "Miércoles" }
        btnJue.setOnClickListener { diaTextView.text = "Jueves" }
        btnVie.setOnClickListener { diaTextView.text = "Viernes" }
        btnSab.setOnClickListener { diaTextView.text = "Sábado" }
        btnDom.setOnClickListener { diaTextView.text = "Domingo" }

        val historialBtn: TextView = view.findViewById(R.id.btn_ver_historial)
        historialBtn.setOnClickListener {
            val intent = Intent(activity, HistorialActivity::class.java)
            startActivity(intent)
        }


        val recyclerView: RecyclerView = view.findViewById(R.id.rvTareas)
        recyclerView.layoutManager = LinearLayoutManager(context)


        val taskList = listOf(
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Hacer comida", "Descripción de hacer comida"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto"),
            Task("Planchar la ropa", "Descripción de planchar la ropa"),
            Task("Ordenar el cuarto", "Descripción de ordenar el cuarto")
        )


        val adapter = TaskAdapter(taskList)
        recyclerView.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance(usuario: Usuario, hogar: Hogar) =
            inicio().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USUARIO, usuario)
                    putSerializable(ARG_HOGAR, hogar)
                }
            }
    }
}