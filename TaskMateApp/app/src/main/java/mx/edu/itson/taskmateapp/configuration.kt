package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USUARIO = "usuario"
private const val ARG_HOGAR = "hogar"

/**
 * A simple [Fragment] subclass.
 * Use the [configuration.newInstance] factory method to
 * create an instance of this fragment.
 */
class configuration : Fragment() {
    // TODO: Rename and change types of parameters
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

        val rootView = inflater.inflate(R.layout.fragment_configuration, container, false)


        val cambiarHogarLayout: LinearLayout = rootView.findViewById(R.id.ll_cambiarHogar)
        val cambiarPerfilLayout: LinearLayout = rootView.findViewById(R.id.ll_cambiarPerfil)


        cambiarHogarLayout.setOnClickListener {
            val intent = Intent(requireContext(), InicioActivity::class.java)
            intent.putExtra("usuario", usuario)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        cambiarPerfilLayout.setOnClickListener {
            val intent = Intent(activity, RegistroActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance(usuario: Usuario, hogar: Hogar) =
            configuration().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USUARIO, usuario)
                    putSerializable(ARG_HOGAR, hogar)
                }
            }
    }
}