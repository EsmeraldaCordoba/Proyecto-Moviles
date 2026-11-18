package cr.ac.utn.nutristatus.domain.model

import java.util.Date

data class Appointment(
    val id: String,
    val patientId: String,
    val doctorName: String,
    val department: String,
    val date: Date,
    val clinicName: String,
    val status: AppointmentStatus,
    val reason: String
)
