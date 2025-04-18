package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USUARIO = "usuario"
private const val ARG_HOGAR = "hogar"

/**
 * A simple [Fragment] subclass.
 * Use the [schedule.newInstance] factory method to
 * create an instance of this fragment.
 */
class schedule : Fragment() {
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

        val rootView = inflater.inflate(R.layout.fragment_schedule, container, false)


        val taskTextView: LinearLayout = rootView.findViewById(R.id.layoutTarea)
        val addButton: ImageView = rootView.findViewById(R.id.fab_add_task)


        taskTextView.setOnClickListener {

            val intent = Intent(activity, AgregarTareaActivity::class.java)
            startActivity(intent)
        }


        addButton.setOnClickListener {

            val intent = Intent(activity, AgregarTareaActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance(usuario: Usuario, hogar: Hogar) =
            schedule().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USUARIO, usuario)
                    putSerializable(ARG_HOGAR, hogar)
                }
            }
    }
}