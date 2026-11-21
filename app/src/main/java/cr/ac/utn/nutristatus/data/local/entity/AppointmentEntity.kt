package cr.ac.utn.nutristatus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import java.util.Date

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey
    val id: String,
    val patientId: String,
    val doctorName: String,
    val department: String,
    val date: Date,
    val clinicName: String,
    val status: AppointmentStatus,
    val reason: String
) {
    fun toDomain(): Appointment {
        return Appointment(
            id = id,
            patientId = patientId,
            doctorName = doctorName,
            department = department,
            date = date,
            clinicName = clinicName,
            status = status,
            reason = reason
        )
    }

    companion object {
        fun fromDomain(appointment: Appointment): AppointmentEntity {
            return AppointmentEntity(
                id = appointment.id,
                patientId = appointment.patientId,
                doctorName = appointment.doctorName,
                department = appointment.department,
                date = appointment.date,
                clinicName = appointment.clinicName,
                status = appointment.status,
                reason = appointment.reason
            )
        }
    }
}
