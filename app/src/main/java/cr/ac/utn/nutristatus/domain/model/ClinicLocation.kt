package cr.ac.utn.nutristatus.domain.model

/**
 * Data class representing a clinic location for mapping.
 */
data class ClinicLocation(
    val id: String,
    val name: String,
    val address: String,
    val officeNumber: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    val operatingHours: String
)
