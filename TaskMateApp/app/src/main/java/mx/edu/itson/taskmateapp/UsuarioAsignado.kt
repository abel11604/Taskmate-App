package mx.edu.itson.taskmateapp

import java.io.Serializable

data class UsuarioAsignado(
    val id_usuario: String,
    val rol: String
) : Serializable
