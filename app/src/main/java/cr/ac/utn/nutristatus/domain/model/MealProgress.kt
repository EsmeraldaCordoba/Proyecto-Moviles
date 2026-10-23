package cr.ac.utn.nutristatus.domain.model

/**
 * Data class representing a food or progress photo entry in the nutritional diary.
 */
data class MealProgress(
    val id: String,
    val title: String,
    val description: String,
    val mealTime: String,
    val mealCategory: String,
    val photoUri: String = "",
    val date: String
)
