package mx.edu.itson.taskmateapp

import java.io.Serializable

data class Tarea(        val id: String,
                    var nombreTarea: String,
                    var descripcion: String): Serializable
