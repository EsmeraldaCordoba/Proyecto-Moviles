package cr.ac.utn.nutristatus.domain.model

enum class AppointmentStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}

data class Appointment(
    val id: String,
    val patientId: String,
    val patientName: String,
    val specialistId: String,
    val specialistName: String,
    val specialistSpecialty: String,
    val date: String, // Format: YYYY-MM-DD
    val time: String, // Format: HH:mm
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val notes: String = "",
    val cancelReason: String? = null
)
