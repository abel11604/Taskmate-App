package mx.edu.itson.taskmateapp

class Hogar(    val id: String,
                var nombreHogar: String,
                var usuariosAsignados: List<UsuarioAsignado>,
                var tareas: List<Tarea>,
                var tareasAsignadas: List<TareaAsignada>
                )
