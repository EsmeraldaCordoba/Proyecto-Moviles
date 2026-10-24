package cr.ac.utn.nutristatus.domain.model

data class MonthlyWeight(
    val monthName: String,
    val weightKg: Float,
)

data class WeightRecord(
    val id: String,
    val currentWeightKg: Float,
    val targetWeightKg: Float,
    val weightDiffKg: Float,
    val lastUpdatedText: String,
    val monthlyHistory: List<MonthlyWeight>,
)
