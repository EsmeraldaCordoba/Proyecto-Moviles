package cr.ac.utn.nutristatus.domain.model

/**
 * Data class representing patient weight tracking progress.
 */
data class WeightProgress(
    val currentWeightKg: Double,
    val goalWeightKg: Double,
    val monthlyChangeKg: Double,
    val weeklyEntries: List<Pair<String, Double>>
)
