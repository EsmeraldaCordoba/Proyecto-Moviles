package cr.ac.utn.nutristatus.domain.model

data class User(
    val id: String,
    val name: String,
    val subtitle: String,
    val avatarUrl: String? = null,
)
