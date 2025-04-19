package mx.edu.itson.taskmateapp

import java.io.Serializable


data class UsuarioAsignado(
    val idUsuario: String,    // ID del usuario
    val rol: String,          // Rol del usuario (Ej. "Administrador", "Habitante")
    val username: String? = null,  // Username opcional, solo cuando se obtenga la información completa
    val correo: String? = null     // Correo opcional, solo cuando se obtenga la información completa
) : Serializable {
    // Constructor secundario con solo idUsuario y rol
    constructor(idUsuario: String, rol: String) : this(idUsuario, rol, null, null)
}