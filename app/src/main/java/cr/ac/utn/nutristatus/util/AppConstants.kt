package cr.ac.utn.nutristatus.util

/**
 * Global constants used throughout the NutriStatus application.
 */
object AppConstants {
    const val CANCELLATION_WARNING_HOURS = 24

    val AVAILABLE_TIME_SLOTS = listOf(
        "09:00 AM",
        "10:30 AM",
        "02:00 PM",
        "04:30 PM"
    )

    val MEAL_CATEGORIES = listOf(
        "Desayuno",
        "Almuerzo",
        "Merienda",
        "Cena"
    )

    // Default Clinic Coordinates (San José, Costa Rica)
    const val DEFAULT_CLINIC_LATITUDE = 9.9333
    const val DEFAULT_CLINIC_LONGITUDE = -84.0833
}
