package mx.edu.itson.taskmateapp

import java.io.Serializable

class Hogar(    val id: String,
                val accesoCodigo:String,
                var nombreHogar: String,
                var usuariosAsignados: List<UsuarioAsignado>,
                var tareas: List<Tarea>,
                var tareasAsignadas: List<TareaAsignada>
                ): Serializable
