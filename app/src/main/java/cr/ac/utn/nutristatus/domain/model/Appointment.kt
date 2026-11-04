package cr.ac.utn.nutristatus.domain.model
import java.util.Date

data class Appointment(
    val id: String,
    val patientId: String,
    val date: Date,
    val doctorName: String,
    val status: AppointmentStatus,
    val reason: String
)
