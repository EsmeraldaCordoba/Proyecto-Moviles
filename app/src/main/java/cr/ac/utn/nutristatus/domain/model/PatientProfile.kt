package cr.ac.utn.nutristatus.domain.model

/**
 * Data class representing basic patient profile information.
 */
data class PatientProfile(
    val id: String,
    val name: String,
    val activePlan: String,
    val assignedNutritionistId: String
)
