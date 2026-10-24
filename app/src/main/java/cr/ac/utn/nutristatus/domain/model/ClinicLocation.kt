package cr.ac.utn.nutristatus.domain.model

data class ClinicLocation(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
    val openingHours: String,
    val imageUrl: String
)
