package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun observeAppointments(): Flow<List<Appointment>>
    suspend fun getAppointments(): List<Appointment>
    suspend fun getAppointmentById(id: String): Appointment?
    suspend fun insertAppointment(appointment: Appointment)
    suspend fun updateAppointment(appointment: Appointment)
    suspend fun deleteAppointment(id: String)
}
