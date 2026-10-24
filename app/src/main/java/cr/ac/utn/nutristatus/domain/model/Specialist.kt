package cr.ac.utn.nutristatus.domain.model

data class Specialist(
    val id: String,
    val name: String,
    val specialty: String, //for example: "Sports Nutrition", "Weight Control", "Functional Nutrition"
    val rating: Float,
    val imageUrl: String,
    val bio: String,
    val consultationFee: Double,
    val availableDays: List<String>
)
