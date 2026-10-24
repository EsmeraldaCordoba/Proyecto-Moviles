package cr.ac.utn.nutristatus.domain.model

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val operatingHours: String,
    val clinicName: String,
    val avatarUrl: String? = null,
)
