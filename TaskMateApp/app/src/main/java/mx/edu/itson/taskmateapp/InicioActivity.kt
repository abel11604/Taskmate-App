package mx.edu.itson.taskmateapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
class InicioActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        val usuario = intent.getSerializableExtra("usuario") as? Usuario
        val editTextHomeName = findViewById<EditText>(R.id.editTextHomeName)
        val btnUnirseHogar = findViewById<LinearLayout>(R.id.btn_unirse_hogar)
        val btnRegistrarHogar = findViewById<LinearLayout>(R.id.btn_registar_hogar)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)
        val codigoCasa = findViewById<EditText>(R.id.editTextHomeCode)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCasas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (usuario != null) {
            db.collection("hogares").get().addOnSuccessListener { result ->
                val hogaresDelUsuario = mutableListOf<Hogar>()

                for (document in result) {
                    val usuariosAsignadosRaw = document.get("usuarios_asignados") as? List<Map<String, Any>> ?: continue
                    if (usuariosAsignadosRaw.any { it["id_usuario"] == usuario.id }) {
                        val hogarId = document.id
                        val accesoCodigo = document.getString("accesoCodigo") ?: ""
                        val nombreHogar = document.getString("nombreHogar") ?: ""

                        db.collection("usuarios").get().addOnSuccessListener { usuariosSnapshot ->
                            val listaUsuarios = usuariosSnapshot.documents.associateBy { it.id }

                            val usuariosAsignados = usuariosAsignadosRaw.map {
                                val idUsuario = it["id_usuario"] as? String ?: ""
                                val rol = it["rol"] as? String ?: ""
                                val datosUsuario = listaUsuarios[idUsuario]
                                UsuarioAsignado(
                                    idUsuario = idUsuario,
                                    rol = rol
                                )
                            }

                            val hogar = Hogar(
                                id = hogarId,
                                accesoCodigo = accesoCodigo,
                                nombreHogar = nombreHogar,
                                usuariosAsignados = usuariosAsignados,
                                tareas = emptyList(),
                                tareasAsignadas = emptyList()
                            )

                            hogaresDelUsuario.add(hogar)

                            if (hogaresDelUsuario.isNotEmpty()) {
                                recyclerView.visibility = View.VISIBLE
                                recyclerView.adapter = HogarAdapter(hogaresDelUsuario, usuario) { hogarSeleccionado ->
                                    val intent = Intent(this, MenuActivity::class.java)
                                    intent.putExtra("usuario", usuario)
                                    intent.putExtra("hogar", hogarSeleccionado)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                Log.e("InicioActivity", "Error al obtener hogares", it)
            }
        }

        btnUnirseHogar.setOnClickListener {
            errorTextView.visibility = View.GONE
            val nombreCasa = editTextHomeName.text.toString().trim()
            if (nombreCasa.isEmpty()) {
                errorTextView.text = "Por favor, asignele un nombre a su hogar"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }
            val accessCode = generateAccessCode()

            val hogarData = hashMapOf(
                "nombreHogar" to nombreCasa,
                "accesoCodigo" to accessCode,
                "usuarios_asignados" to listOf(
                    hashMapOf(
                        "id_usuario" to (usuario?.id ?: ""),
                        "rol" to "Administrador"
                    )
                ),
                "tareas" to emptyList<Any>(),
                "tareasAsignadas" to emptyList<Any>()
            )

            db.collection("hogares")
                .add(hogarData)
                .addOnSuccessListener { documentRef ->
                    val hogar = Hogar(
                        id = documentRef.id,
                        accesoCodigo = accessCode,
                        nombreHogar = nombreCasa,
                        usuariosAsignados = listOf(
                            UsuarioAsignado(
                                idUsuario = usuario?.id ?: "",
                                rol = "Administrador"
                            )
                        ),
                        tareas = emptyList(),
                        tareasAsignadas = emptyList()
                    )

                    val intent = Intent(this, MenuActivity::class.java)
                    intent.putExtra("hogar", hogar)
                    intent.putExtra("usuario", usuario)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    errorTextView.text = "Error al crear el hogar: ${e.message}"
                    errorTextView.visibility = View.VISIBLE
                }
        }

        btnRegistrarHogar.setOnClickListener {
            errorTextView.visibility = View.GONE
            val casaCodigo = codigoCasa.text.toString().trim()
            if (casaCodigo.isEmpty()) {
                errorTextView.text = "Por favor, inserte un código para unirse a un hogar"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            db.collection("hogares")
                .whereEqualTo("accesoCodigo", casaCodigo)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        errorTextView.text = "No se encontró un hogar con ese código"
                        errorTextView.visibility = View.VISIBLE
                    } else {
                        val hogarDoc = querySnapshot.documents[0]
                        val hogarId = hogarDoc.id

                        val usuarioAsignado = hashMapOf(
                            "id_usuario" to (usuario?.id ?: ""),
                            "rol" to "Habitante"
                        )

                        hogarDoc.reference.update("usuarios_asignados",
                            com.google.firebase.firestore.FieldValue.arrayUnion(usuarioAsignado))
                            .addOnSuccessListener {
                                val nombreHogar = hogarDoc.getString("nombreHogar") ?: ""
                                val accesoCodigo = hogarDoc.getString("accesoCodigo") ?: ""

                                val hogar = Hogar(
                                    id = hogarId,
                                    accesoCodigo = accesoCodigo,
                                    nombreHogar = nombreHogar,
                                    usuariosAsignados = emptyList(),
                                    tareas = emptyList(),
                                    tareasAsignadas = emptyList()
                                )

                                val intent = Intent(this, MenuActivity::class.java)
                                intent.putExtra("hogar", hogar)
                                intent.putExtra("usuario", usuario)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                errorTextView.text = "Error al unirse al hogar: ${e.message}"
                                errorTextView.visibility = View.VISIBLE
                            }
                    }
                }
                .addOnFailureListener { e ->
                    errorTextView.text = "Error al buscar hogar: ${e.message}"
                    errorTextView.visibility = View.VISIBLE
                }
        }
    }

    private fun generateAccessCode(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..4).map { charset.random() }.joinToString("")
    }
}
