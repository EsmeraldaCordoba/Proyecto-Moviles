package cr.ac.utn.nutristatus.domain.model

/**
 * Data class representing a specialist nutritionist in the directory.
 */
data class Nutritionist(
    val id: String,
    val name: String,
    val specialty: String,
    val approach: NutritionistApproach,
    val bio: String,
    val rating: Double,
    val reviewCount: Int,
    val clinicAddress: String,
    val operatingHours: String,
    val photoUrl: String = ""
)
