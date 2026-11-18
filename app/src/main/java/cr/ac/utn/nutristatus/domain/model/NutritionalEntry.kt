package cr.ac.utn.nutristatus.domain.model

import java.util.Date

data class NutritionalEntry(
    val id: String,
    val title: String,
    val description: String,
    val imageUri: String?,
    val timestamp: Date
)
