package cr.ac.utn.nutristatus.domain.model

data class Appointment(
    val id: String,
    val doctorName: String,
    val department: String,
    val dateTime: String,
    val clinicName: String,
    val status: AppointmentStatus,
    val notes: String? = null,
)
