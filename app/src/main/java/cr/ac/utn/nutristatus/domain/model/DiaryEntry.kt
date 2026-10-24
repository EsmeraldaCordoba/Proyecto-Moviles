package cr.ac.utn.nutristatus.domain.model

data class DiaryEntry(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val mealType: String,
    val imageUrl: String? = null,
)
