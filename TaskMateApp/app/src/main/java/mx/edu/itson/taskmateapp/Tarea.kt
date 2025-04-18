package mx.edu.itson.taskmateapp

import java.io.Serializable

class Tarea(        val id: String,
                    var nombreTarea: String,
                    var descripcion: String): Serializable
