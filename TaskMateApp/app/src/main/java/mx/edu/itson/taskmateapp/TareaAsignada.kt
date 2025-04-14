package mx.edu.itson.taskmateapp

import java.time.LocalDateTime

data class TareaAsignada(
    val id: String,
    val tarea: Tarea,
    var usuariosAsignados: List<String>,
    var horaRealizacion: LocalDateTime,
    var estado: String,
    val hogarId: String
)