package cr.ac.utn.nutristatus.domain.model

/**
 * Data class representing a nutritional appointment.
 */
data class Appointment(
    val id: String,
    val patientName: String,
    val nutritionistId: String,
    val nutritionistName: String,
    val specialty: String,
    val dateTime: String,
    val timeSlot: String,
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val clinicLocationName: String,
    val notes: String = "",
    val cancellationReason: String? = null
)
